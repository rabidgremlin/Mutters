import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Intent;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.opennlp.ner.OpenNLPSlotMatcher;
import com.rabidgremlin.mutters.opennlp.intent.OpenNLPTokenizer;
import org.junit.Test;
import opennlp.tools.tokenize.SimpleTokenizer;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.util.HashMap;

public class SlotMatcherTests
{
  @Test
  public void when_default_slot_does_not_match_return_default_value()
  {
    OpenNLPSlotMatcher slotMatcher = new OpenNLPSlotMatcher(new OpenNLPTokenizer(SimpleTokenizer.INSTANCE));
    slotMatcher.addSlotModel("testSlot", "models/en-ner-persons.bin");
    Intent testIntent = new Intent("testIntent");
    TestSlot testSlot = new TestSlot("testSlot");
    testIntent.addSlot(testSlot);
    HashMap<Slot, SlotMatch> slotSlotMatchHashMap = slotMatcher.match(new Context(), testIntent, "testUtterance");

    SlotMatch slotMatch = slotSlotMatchHashMap.get(testSlot);
    assertThat(slotMatch.getValue(), is("Default value"));
  }
}
