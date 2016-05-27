package indi.yume.tools.adapter_renderer.recycler.headerfooter;

import org.junit.Test;

import java.util.Random;

import indi.yume.tools.adapter_renderer.recycler.headerfooter.IndexTooLargeException;
import indi.yume.tools.adapter_renderer.recycler.headerfooter.OtherViewManger;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    @Test
    public void testMock() {
        Random random = new Random();

        for(int i = 0; i < 999; i++) {
            int index = Math.abs(random.nextInt());

            if(index > 0xfffffff) {
                try {
                    OtherViewManger.getMockType(OtherViewManger.HEADER_VIEW_TYPE, index);
                    assert false;
                } catch (IndexTooLargeException e) {

                }
            } else {
                int mockType = OtherViewManger.getMockType(OtherViewManger.HEADER_VIEW_TYPE, index);

                assertEquals(OtherViewManger.HEADER_VIEW_TYPE, OtherViewManger.getType(mockType));
                assertEquals(index, OtherViewManger.getIndex(mockType));
            }
        }
    }
}