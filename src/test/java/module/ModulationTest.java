package module;

import signal.ModulationSignal;
import org.junit.Before;

import static junit.framework.TestCase.assertEquals;

public class ModulationTest extends AbstractSignalTest {

    @Before
    public void init(){
        signal = new ModulationSignal();
    }
//
//
//    @Override
//    @Test
//    public void increaseUntilLimit() {
//
//        for (int i= 0; i <=10;i++){
//            signal.addVoltage(1);
//            Assert.assertTrue("Voltage must be less than : "+i, signal.getVolt() <= 10);
//        }
//
//        signal = new ModulationSignal();
//        for (int i= -10; i <=0;i++){
//            signal.addVoltage(-1);
//            Assert.assertTrue("Voltage must be less than : "+i, signal.getVolt() >=-10);
//        }
//
//
//    }
//
//    @Override
//    @Test
//    public void moreThanLimit() {
//        signal.addVoltage(10);
//        assertEquals("Voltage max : 10",  10.0,signal.getVolt());
//        signal.addVoltage(1);
//        assertEquals("Voltage max : 10",  10.0,signal.getVolt());
//
//    }
//
//    @Override
//    @Test
//    public void lessThanLimit() {
//        signal.addVoltage(-10);
//        assertEquals("Voltage max : -10", -10.0, signal.getVolt());
//        signal.addVoltage(-1);
//        assertEquals("Voltage max : -10", -10.0, signal.getVolt());
//
//    }


}
