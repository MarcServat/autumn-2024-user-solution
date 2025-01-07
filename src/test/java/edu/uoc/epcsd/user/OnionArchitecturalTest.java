package edu.uoc.epcsd.user;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.junit.AnalyzeClasses;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.onionArchitecture;

@AnalyzeClasses(packages = {
        "edu.uoc.epcsd.user",
})
public class OnionArchitecturalTest {
    @ArchTest
    ArchRule onion_architecture_is_respected = onionArchitecture()
            .domainModels("..domain..")
            .domainServices("..domain.service..")
            .adapter("rest", "..application.rest..")
            .adapter("jpa", "..infrastructure.repository.jpa..")
            .applicationServices("..");

    @ArchTest
    ArchRule ServiceNaming = classes().that()
            .areAnnotatedWith(Service.class)
            .should().haveSimpleNameEndingWith("ServiceImpl");
}
