package uilogic;

public class HistoryConsole {
    private int viewIndex;
    private int historyPage;

    public HistoryConsole() {
        reset();
    }

    public void reset() {
        viewIndex = 0;
        historyPage = 0;
    }

    public void syncWithNewMove(int historySize) {
        viewIndex = historySize - 1;
    }

    public void viewPrevious() {
        if (viewIndex > 0) viewIndex--;
    }

    public void viewNext(int historySize) {
        if (viewIndex >= 0 && viewIndex < historySize - 1) viewIndex++;
    }

    public void viewCurrent(int historySize) {
        if (historySize > 0) viewIndex = historySize - 1;
    }

    public boolean isViewingPast(int historySize) {
        return viewIndex >= 0 && viewIndex < historySize - 1;
    }

    public int getViewIndex() { return viewIndex; }
    public int getHistoryPage() { return historyPage; }
    public void setHistoryPage(int page) { this.historyPage = page; }
    public void resetHistoryPage() { this.historyPage = 0; }
}
