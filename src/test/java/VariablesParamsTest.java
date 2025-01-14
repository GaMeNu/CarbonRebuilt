import me.gamenu.carbondf.code.TemplateManager;
import me.gamenu.carbondf.exceptions.TypeException;
import me.gamenu.carbondf.values.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VariablesParamsTest {

    @Test
    void paramAccepts() {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();
        DFParameter param = new DFParameter.Builder(vm, "dingus", new TypeSet(DFItem.Type.STRING, DFItem.Type.NUMBER))
                .setOptional(true)
                .setDefaultValue(new DFString("aaa"))
                .build();

        DFString stringValue = new DFString("womp womp");
        DFNumber numberValue = new DFNumber(69);
        DFParticle particleValue = new DFParticle("SPIT");
        DFVariable typVar = vm.typed("typVar", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER, DFItem.Type.PARTICLE));
        DFVariable typFVar = vm.typed("typFVar", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.PARTICLE));

        assertTrue(param.canAcceptItem(stringValue));
        assertTrue(param.canAcceptItem(numberValue));
        assertFalse(param.canAcceptItem(particleValue));
        assertTrue(param.canAcceptItem(typVar));
        assertFalse(param.canAcceptItem(typFVar));

        DFVariable dynVar = vm.dynamic("dynVar", DFVariable.Scope.LINE);
        assertTrue(param.canAcceptItem(dynVar));

        dynVar.setValue(new DFSound("UI_TOAST_IN"));
        assertFalse(param.canAcceptItem(dynVar));

        dynVar.setValue(new DFGameValue("CPU Usage"));
        assertTrue(param.canAcceptItem(dynVar));
        vm.clearLineScope();
    }

    @Test
    void returnParamAccepts() {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        DFParameter testParam = vm.param("testParam", DFItem.Type.NUMBER)
                .setReturned(true)
                .build();

        DFVariable test = vm.typed("test", DFVariable.Scope.LINE, DFItem.Type.NUMBER);
        DFVariable test2 = vm.dynamic("test2", DFVariable.Scope.LINE)
                .setValue(new DFNumber(1));
        assertTrue(testParam.canAcceptItem(test));
        assertTrue(testParam.canAcceptItem(test2));

        test2.setValue(new DFString("a"));
        assertFalse(testParam.canAcceptItem(test2));
        assertFalse(testParam.canAcceptItem(new DFNumber(1)));
    }

    @Test
    void buildParamVars() {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        String PARAM_NAME = "dingus";
        DFParameter param = new DFParameter.Builder(vm, PARAM_NAME, new TypeSet(DFItem.Type.STRING))
                .setOptional(true)
                .setDefaultValue(new DFString("aaa"))
                .build();

        DFVariable var = param.getVariable();
        DFVariable var2 = param.getVariable();
        assertEquals(var, var2);
        assertEquals(var.getName(), PARAM_NAME);
        vm.clearLineScope();
    }

    @Test
    void realtimeTypeAssignment() {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        DFVariable src = vm.typed("src", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER));
        DFVariable mid = vm.typed("mid", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER, DFItem.Type.STRING));
        DFVariable dst = vm.typed("dst", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.STRING));

        src.setValue(new DFNumber(1));
        mid.setValue(src);
        assertEquals(src.getRuntimeType(), mid.getRuntimeType());

        assertThrows(TypeException.class, () -> dst.setValue(mid));
        vm.clearLineScope();
    }

    @Test
    void realtimeTypeAssignmentDynamic() {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        DFVariable src = vm.typed("src", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER, DFItem.Type.STRING));
        DFVariable dyn = vm.dynamic("dyn", DFVariable.Scope.LINE);
        DFVariable dst1 = vm.typed("dst1", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER));

        src.setValue(new DFNumber(1));
        dyn.setValue(src);

        assertEquals(src.getType(), dyn.getType());
        vm.clearLineScope();
    }

    @Test
    void assignGameValues() {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        DFVariable var = vm.typed("var", DFVariable.Scope.LINE, new TypeSet(DFItem.Type.NUMBER));
        var.setValue(new DFGameValue("CPU Usage"));

        assertTrue(var.getType().contains(DFItem.Type.NUMBER));
        vm.clearLineScope();
    }

    @Test
    void acceptGameValues() {
        TemplateManager tm = new TemplateManager();
        VarManager vm = tm.vars();

        DFParameter param = vm.param("param", DFItem.Type.NUMBER)
                .build();

        assertTrue(param.canAcceptItem(new DFGameValue("CPU Usage")));
        vm.clearLineScope();
    }


}
