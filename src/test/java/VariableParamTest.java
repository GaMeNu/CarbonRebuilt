import me.gamenu.carbondf.exceptions.TypeException;
import me.gamenu.carbondf.values.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VariableParamTest {

    @Test
    void paramAccepts() {
        DFParameter param = new DFParameter.Builder("dingus", new TypeSet(DFItem.Type.STRING, DFItem.Type.NUMBER))
                .setOptional(true)
                .setDefaultValue(new DFString("aaa"))
                .build();

        DFString stringValue = new DFString("womp womo");
        DFNumber numberValue = new DFNumber(69);
        DFParticle particleValue = new DFParticle("SPIT");

        assertTrue(param.canAcceptItem(stringValue));
        assertTrue(param.canAcceptItem(numberValue));
        assertFalse(param.canAcceptItem(particleValue));
    }

    @Test
    void paramVars() {
        String PARAM_NAME = "dingus";
        DFParameter param = new DFParameter.Builder(PARAM_NAME, new TypeSet(DFItem.Type.STRING))
                .setOptional(true)
                .setDefaultValue(new DFString("aaa"))
                .build();

        DFVariable var = param.buildVariable();
        DFVariable var2 = param.buildVariable();
        assertEquals(var, var2);
        assertEquals(var.getName(), PARAM_NAME);
    }

    @Test()
    void realtimeTypeAssignment() {
        DFVariable src = DFVariable.typed("src", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER));
        DFVariable mid = DFVariable.typed("mid", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER, DFItem.Type.STRING));
        DFVariable dst = DFVariable.typed("dst", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.STRING));

        src.setValue(new DFNumber(1));
        mid.setValue(src);
        assertEquals(src.getValue(), mid.getValue());

        assertThrows(TypeException.class, () -> dst.setValue(mid));
    }
}
