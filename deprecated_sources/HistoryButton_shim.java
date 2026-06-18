package uilogic;

/**
 * HistoryButton removed. Use {@link RewindButton} instead.
 *
 * This shim exists only to produce a clear runtime message if some legacy code still tries to instantiate
 * HistoryButton. New code should use RewindButton directly; the wrapper has been removed.
 */
@Deprecated
public final class HistoryButton {
    private HistoryButton() {
        throw new UnsupportedOperationException("HistoryButton was removed; use RewindButton instead.");
    }
}

