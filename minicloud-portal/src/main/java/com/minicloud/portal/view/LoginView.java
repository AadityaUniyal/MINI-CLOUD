package com.minicloud.portal.view;

import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

/**
 * Enhanced LoginView with AWS Console Branding.
 */
@Route("login")
@PageTitle("Login | MiniCloud Console")
@AnonymousAllowed
@StyleSheet("context://styles/minicloud-theme.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm loginForm = new LoginForm();

    public LoginView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("login-view");

        H1 title = new H1();
        Span mini = new Span("Mini");
        Span cloud = new Span("Cloud");
        cloud.getStyle().set("color", "var(--aws-orange)");
        title.add(mini, cloud);
        title.getStyle().set("font-size", "2.5rem").set("margin-bottom", "0.5rem");

        Paragraph subtitle = new Paragraph("Sign in to your MiniCloud Management Console");
        subtitle.getStyle().set("color", "var(--aws-text-sub)").set("margin-bottom", "2rem");

        loginForm.setAction("login");

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.getForm().setTitle("Sign In");
        i18n.getForm().setUsername("Cloud Identity (Username/Email)");
        i18n.getErrorMessage().setTitle("Authentication Failed");
        i18n.getErrorMessage().setMessage("The credentials provided are invalid. Please check and try again.");
        loginForm.setI18n(i18n);

        VerticalLayout loginBox = new VerticalLayout(title, subtitle, loginForm);
        loginBox.setAlignItems(Alignment.CENTER);
        loginBox.setPadding(true);
        loginBox.getStyle()
                .set("background", "var(--aws-card-bg)")
                .set("border", "1px solid var(--aws-border)")
                .set("border-radius", "12px")
                .set("box-shadow", "0 8px 24px rgba(0,0,0,0.12)")
                .set("max-width", "450px");

        add(loginBox);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if (event.getLocation().getQueryParameters().getParameters().containsKey("error")) {
            loginForm.setError(true);
            Notification.show("Access Denied — invalid credentials.", 4000,
                    Notification.Position.TOP_CENTER);
        }
    }
}
