package indi.yume.tools.adapter_renderer.recycler.headerfooter;

/**
 * Created by yume on 16-5-25.
 */

public class IndexTooLargeException extends RuntimeException {
    IndexTooLargeException() {
        super("index is too large: 268435455");
    }
}
