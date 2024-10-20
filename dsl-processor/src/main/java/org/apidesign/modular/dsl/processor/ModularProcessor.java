package org.apidesign.modular.dsl.processor;

import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import org.apidesign.modular.info.ModuleInfo;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.openide.util.lookup.ServiceProvider;

@ServiceProvider(service = Processor.class)
public final class ModularProcessor extends AbstractProcessor {

    public ModularProcessor() {
        System.err.println("created");
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        var s = new TreeSet<String>();
        s.add(ModuleInfo.class.getName());
        return s;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var e : roundEnv.getElementsAnnotatedWith(ModuleInfo.class)) {
            try {
                var name = e.getAnnotation(ModuleInfo.class).name();
                var pkg =processingEnv.getElementUtils().getPackageOf(e).toString();
                var clz = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "module-info.class");
                generateModuleInfo(clz, name);
                processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Generated " + clz.getName(), e);
            } catch (IOException ex) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, ex.getMessage(), e);
                ex.printStackTrace();
                return false;
            }
        }
        return true;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latest();
    }

    private static void generateModuleInfo(FileObject to, String name) throws IOException {
        ClassWriter cw = new ClassWriter(Opcodes.V11);
        cw.visit(64, Opcodes.ACC_MODULE, "module-info", null, null, null);
        var mv = cw.visitModule(name, 0, null);
        mv.visitRequire("java.base", 0, null);
        mv.visitEnd();
        cw.visitEnd();
        try (java.io.OutputStream os = to.openOutputStream()) {
            os.write(cw.toByteArray());
        }
    }
}
