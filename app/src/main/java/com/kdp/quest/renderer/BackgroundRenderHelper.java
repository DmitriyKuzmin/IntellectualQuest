
package com.kdp.quest.renderer;

import com.maxst.ar.TrackedImage;

public class BackgroundRenderHelper {

    private BackgroundRenderer backgroundRenderer;

    public void drawBackground(TrackedImage trackedImage, float[] matrix) {
        if (backgroundRenderer == null)
            backgroundRenderer = new Yuv420spRenderer();

        backgroundRenderer.setProjectionMatrix(matrix);
        backgroundRenderer.draw(trackedImage);
    }
}
