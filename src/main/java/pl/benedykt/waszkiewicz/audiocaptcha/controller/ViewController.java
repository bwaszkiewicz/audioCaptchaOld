package pl.benedykt.waszkiewicz.audiocaptcha.controller;

public interface ViewController {
    void init();
    Boolean submit();
    void refresh();
}
