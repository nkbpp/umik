package ru.pfr.configuration;

import jodd.http.HttpUtil;
import jodd.http.HttpValuesMap;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.pfr.Application;
import ru.pfr.model.umikbd.Adminparam;
import ru.pfr.model.umikbd.Logi;
import ru.pfr.model.umikbd.User;
import ru.pfr.service.bdumik.AdminparamService;
import ru.pfr.service.bdumik.LogiService;
import ru.pfr.service.bdumik.RayonService;
import ru.pfr.service.bdumik.UserService;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AuthProvider implements AuthenticationProvider {
    @Autowired
    private RayonService rayonService;

    @Autowired
    private AdminparamService adminparamService;

    @Autowired
    LogiService logiService;

    @Autowired
    UserService userService;

    private static final Logger logger = LogManager.getLogger(Application.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("Инициализация контекста безопасности и создание объекта UsernamePasswordAuthenticationToken с переданными данными.");
        SecurityContext context = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
        String username = String.valueOf(auth.getPrincipal());
        String password = String.valueOf(auth.getCredentials());

        logger.info("Формирование параметров для HTTP-запроса для аутентификации");
        Map<String, String> parameterList = new HashMap<>();
        parameterList.put("adr", "http://127.0.0.0/Autent");
        parameterList.put("kod", "161"); // Номер программы
        parameterList.put("login", username);
        parameterList.put("pass", password);

        logiService.save(new Logi(LocalDateTime.now(), username, "Попытка авторизации по логину " + username + " AuthProvider authenticate()"));
        logger.info("Попытка авторизации по логину " + username + " AuthProvider authenticate()");

        logger.info("Получение ответа от внешнего сервиса и обработка заголовков ответа.");
        CloseableHttpResponse httpResponse = getHTTPResponse("http://10.41.0.247:322/ACS/AutentAll", parameterList);
        Header[] headers = httpResponse.getHeaders("Location");

        User logerr = Optional.ofNullable(userService.findByLoginuser(username)).orElseGet(() -> {
            User newUser = new User(username, rayonService.findByKod("1000").get());
            userService.save(newUser);
            return newUser;
        });

        Adminparam adminparam = adminparamService.findByAdminparam();
        Long datenow = new Date().getTime() + 10800000L; // Избавление от погрешности во времени

        if (logerr.getActive() >= adminparam.getKolpopitok() &&
                logerr.getActive() < adminparam.getBlock() &&
                (datenow - logerr.getDate().getTime()) <= (600000L + ((adminparam.getKolpopitok() - 2) * adminparam.getKoefpopitok() * 60000L))) {
            handleFailedAttempt(logerr, username, datenow, "Превышен лимит попыток");
        }

        if (logerr.getActive() >= adminparam.getBlock()) {
            handleFailedAttempt(logerr, username, datenow, "Пользователь заблокирован");
        }

        if (headers.length == 0) {
            handleFailedAttempt(logerr, username, datenow, "Пароль неверен");
        } else {
            resetFailedAttempts(logerr, datenow);
        }

        String response = headers[0].getValue();
        Matcher authQueryString = Pattern.compile("^http://127\\.0\\.0\\.0/Autent\\?([^\\r\\n]++)$").matcher(response);
        if (!authQueryString.find()) {
            throw new BadCredentialsException("Пароль неверен");
        }

        HttpValuesMap<Object> authData = HttpUtil.parseQuery(authQueryString.group(1), true);
        Collection<GrantedAuthority> roleList = new HashSet<>();
        Object[] rights = authData.get("right");
        String userId = (String) authData.get("id")[0];
        String upfrCode = (String) authData.get("upfr")[0];

        for (Object right : rights) {
            Integer rightCode = Integer.parseInt((String) right);
            switch (rightCode) {
                case 3000:
                    roleList.add(new SimpleGrantedAuthority("ROLE_OPFR"));
                    break;
                case 3002:
                    roleList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                    upfrCode = "999";
                    break;
            }
        }

        while (upfrCode.length() < 3) {
            upfrCode = "0" + upfrCode;
        }

        User user = userService.findByLoginuser(username);
        user.setActive(1L);
        user.setRayon(rayonService.findByKod(upfrCode).get());
        userService.save(user);

        authentication = new UsernamePasswordAuthenticationToken(user, "", roleList);
        context.setAuthentication(authentication);
        logiService.save(new Logi(LocalDateTime.now(), user.getLogin(),"Пользователь " + user.getLogin() + " авторизован  AuthProvider authenticate()"));
        logger.info("Пользователь " + user.getLogin() + " авторизован  AuthProvider authenticate()");

        return authentication;    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(UsernamePasswordAuthenticationToken.class);
    }

    /**
     * Обрабатывают неудачные попытки авторизации
     */
    private void handleFailedAttempt(User logerr, String username, Long datenow, String message) {
        logerr.setActive(logerr.getActive() + 1);
        logerr.setDate(new Date(datenow));
        userService.save(logerr);
        logiService.save(new Logi(LocalDateTime.now(), username, "Попытка авторизации " + message + " количество попыток " + logerr.getActive() + " AuthProvider authenticate()"));
        logger.info("Попытка авторизации по логину " + username + " " + message + " количество попыток " + logerr.getActive() + " AuthProvider authenticate()");
        throw new BadCredentialsException(message);
    }

    /**
     * Сбрасывает счетчик неудачных попыток
     */
    private void resetFailedAttempts(User logerr, Long datenow) {
        logerr.setActive(0L);
        logerr.setDate(new Date(datenow));
        userService.save(logerr);
    }


    /**
     * Отправляет HTTP-запрос к внешнему сервису для проверки аутентификационных данных пользователя.
     * @param addr
     * @param parameterList
     * @return
     */
    public CloseableHttpResponse getHTTPResponse(String addr, Map<String, String> parameterList) {
        try {
            BasicCookieStore cookieStore = new BasicCookieStore();
            CloseableHttpClient httpclient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            HttpUriRequest req;
            CloseableHttpResponse response;
            RequestConfig requestConfig = RequestConfig
                    .copy(RequestConfig.DEFAULT)
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .build();
            RequestBuilder reqBuilder = RequestBuilder.post().setUri(new URI(addr));
            for (String key : parameterList.keySet()) {
                reqBuilder.addParameter(key, parameterList.get(key));
            }
            req = reqBuilder.setConfig(requestConfig).build();
            response = httpclient.execute(req);
            return response;
        } catch (URISyntaxException | IOException ex) {
            return null;
        }
    }
}
