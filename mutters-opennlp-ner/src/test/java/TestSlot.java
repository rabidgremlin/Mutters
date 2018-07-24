import com.rabidgremlin.mutters.core.Context;
import com.rabidgremlin.mutters.core.Slot;
import com.rabidgremlin.mutters.core.SlotMatch;
import com.rabidgremlin.mutters.slots.DefaultValueSlot;
import static org.junit.Assert.assertThat;

public class TestSlot
        extends Slot implements DefaultValueSlot
{

    private String name;

    public TestSlot(String name)
    {
        this.name = name;
    }

    @Override
    public Object getDefaultValue() {
        return "Default value";
    }

    @Override
    public SlotMatch match(String token, Context context)
    {
        return null;
    }

    @Override
    public String getName()
    {
        return name;
    }
}
