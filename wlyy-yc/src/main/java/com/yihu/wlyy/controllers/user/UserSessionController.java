package com.yihu.wlyy.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yihu.wlyy.controllers.BaseController;
import com.yihu.wlyy.models.user.UserAgent;
import com.yihu.wlyy.models.user.UserSessionModel;
import com.yihu.wlyy.services.user.UserSessionService;
import com.yihu.wlyy.util.StringUtil;
import com.yihu.wlyy.util.SystemConf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @created Airhead 2016/9/4.
 */
@Controller
@RequestMapping(value = "/login")
public class UserSessionController extends BaseController {
    @Autowired
    private UserSessionService userSessionService;

    @RequestMapping(value = "/wechat", method = RequestMethod.GET)
    public void loginWeChat(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String openId = request.getParameter("openId");

        if (StringUtil.isEmpty(openId)) {
            return;
        }
        response.sendRedirect(userSessionService.genEHomeUrl(openId));
    }

    @RequestMapping(value = "/wechat/callback", method = RequestMethod.GET)
    public void weChatCallback(HttpServletRequest request, HttpServletResponse response) {
        String code = request.getParameter("code");
        String message = request.getParameter("message");
        String openId = request.getParameter("openid");
        String sig = request.getParameter("sig");

        UserSessionModel userSessionModel = userSessionService.loginWeChat(code, message, openId, sig);
        try {
            if (userSessionModel == null) {
                String loginFail = SystemConf.getInstance().getValue("loginFail");
                response.sendRedirect(loginFail+"?openId="+openId);
                return;
            }
            String familyDoctorUrl = SystemConf.getInstance().getValue("familyDoctor");
            UserAgent userAgent = new UserAgent();
            userAgent.setToken(userSessionModel.getToken());
            userAgent.setUid(userSessionModel.getUserCode());
            userAgent.setOpenid(openId);
            ObjectMapper objectMapper = new ObjectMapper();
            String user = objectMapper.writeValueAsString(userAgent);

            response.sendRedirect(familyDoctorUrl+"?userAgent="+user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //只是为了拦截前端信息
    @RequestMapping(value = "/app", method = RequestMethod.GET)
    public void loginApp(HttpServletRequest request, HttpServletResponse response) {
    }
}
