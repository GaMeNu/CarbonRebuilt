import me.gamenu.carbondf.exceptions.TypeException;
import me.gamenu.carbondf.values.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VariablesParamsTest {

    @Test
    void paramAccepts() {
        DFParameter param = new DFParameter.Builder("dingus", new TypeSet(DFItem.Type.STRING, DFItem.Type.NUMBER))
                .setOptional(true)
                .setDefaultValue(new DFString("aaa"))
                .build();

        DFString stringValue = new DFString("womp womp");
        DFNumber numberValue = new DFNumber(69);
        DFParticle particleValue = new DFParticle("SPIT");
        DFVariable typVar = DFVariable.typed("typVar", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER, DFItem.Type.PARTICLE));
        DFVariable typFVar = DFVariable.typed("typFVar", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.PARTICLE));

        assertTrue(param.canAcceptItem(stringValue));
        assertTrue(param.canAcceptItem(numberValue));
        assertFalse(param.canAcceptItem(particleValue));
        assertTrue(param.canAcceptItem(typVar));
        assertFalse(param.canAcceptItem(typFVar));

        DFVariable dynVar = DFVariable.dynamic("dynVar", DFVariable.Scope.LINE);
        assertTrue(param.canAcceptItem(dynVar));

        dynVar.setValue(new DFSound("UI_TOAST_IN"));
        assertFalse(param.canAcceptItem(dynVar));

        dynVar.setValue(new DFGameValue("CPU Usage"));
        assertTrue(param.canAcceptItem(dynVar));
    }

    @Test
    void buildParamVars() {
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

    @Test
    void assignGameValues() {
        DFVariable var = DFVariable.typed("var", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER));
        var.setValue(new DFGameValue("CPU Usage"));

        assertTrue(var.getType().contains(DFItem.Type.NUMBER));
        assertEquals(var.getValue().getRealType(), DFItem.Type.GAME_VALUE);
    }

    @Test
    void acceptGameValues() {
        DFParameter param = new DFParameter.Builder("param", new TypeSet(DFItem.Type.NUMBER))
                .build();

        assertTrue(param.canAcceptItem(new DFGameValue("CPU Usage")));

    }


}
