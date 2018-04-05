import org.junit.Test;

public class SenderTest {

    @Test
    public final void sending() throws Exception {
        JarCommunicator.init();
        Thread.sleep(1000);
        JarCommunicator.send(1522950630012L, "Test-Data");
        Thread.sleep(2000);
    }

}
