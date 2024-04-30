package com.sendgrid.oai.template;

public interface IApiActionTemplate {
    void clean();

    void add(String template);

    void addSupportVersion();
}
