package org.apidesign.modular.dsl.processor;

import java.io.IOException;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import org.apidesign.modular.info.ModuleInfo;

public final class ModularProcessor extends AbstractProcessor {

    public ModularProcessor() {
        System.err.println("created");
    }


    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of(ModuleInfo.class.getName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (var e : roundEnv.getElementsAnnotatedWith(ModuleInfo.class)) {
            try {
                var name = e.getAnnotation(ModuleInfo.class).name();
                var pkg =processingEnv.getElementUtils().getPackageOf(e).toString();
                        System.err.println("PKG: " + pkg);
                var clz = processingEnv.getFiler().createResource(StandardLocation.SOURCE_OUTPUT, "", "module-info.java");
//                var clz = processingEnv.getFiler().createResource(StandardLocation.CLASS_OUTPUT, "", "module-info.class");
                System.err.println("writing to " + clz);
                var w = clz.openWriter();
                w.write("module " + name +" {\n");
                w.write("  requires static org.apidesign.modular.info;\n");
                w.write("}\n");
                w.close();
            } catch (IOException ex) {
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

}
