
module org.apidesign.modular.dsl.processor {
    requires static org.apidesign.modular.info;
    requires java.compiler;

    provides javax.annotation.processing.Processor with
        org.apidesign.modular.dsl.processor.ModularProcessor;
}
