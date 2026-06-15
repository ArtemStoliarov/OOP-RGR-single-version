package uilogic;

public interface GameInterface {
    void resign();
    void reset();
    void toggleInfo();
    void setInfoTab(int tab);
    boolean isShowInfo();
    int getInfoTab();
    boolean isGameOver();
    int getHistorySize(); // Залишаємо тільки запит розміру даних
}
