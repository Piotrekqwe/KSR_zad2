package ksr.pl.kw.service.calculator;

import ksr.pl.kw.model.Quantifier;
import ksr.pl.kw.model.traits.Trait;
import ksr.pl.kw.model.traits.TraitId;
import ksr.pl.kw.service.FileDeserializationService;
import ksr.pl.kw.service.FileSerializationService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CalculatorStateSerializer {
    private static final String ABSOLUTE_QUANTIFIERS_PATH = "data\\absolute_quantifiers.bin";
    private static final String RELATIVE_QUANTIFIERS_PATH = "data\\relative_quantifiers.bin";
    private static final String TRAIT_FILE_PATH = "data\\traits.bin";
    private final FileSerializationService serializationService = new FileSerializationService();
    private final FileDeserializationService deserializationService = new FileDeserializationService();


    Calculator loadCalculator() {
        List<Trait> traits = (List<Trait>) deserializationService.load(TRAIT_FILE_PATH).orElseGet(() -> Arrays.stream(TraitId.values()).map(Trait::new).collect(Collectors.toList()));
        Quantifier relativeQuantifiers = (Quantifier) deserializationService.load(RELATIVE_QUANTIFIERS_PATH).orElseGet(() -> new Quantifier(Quantifier.RELATIVE_QUANTIFIERS_NAME));
        Quantifier absoluteQuantifiers = (Quantifier) deserializationService.load(ABSOLUTE_QUANTIFIERS_PATH).orElseGet(() -> new Quantifier(Quantifier.ABSOLUTE_QUANTIFIERS_NAME));
        return new Calculator(traits, relativeQuantifiers, absoluteQuantifiers);
    }

    public void saveCalculatorState(Calculator calculator) {
        serializationService.save(TRAIT_FILE_PATH, calculator.getTraits());
        serializationService.save(RELATIVE_QUANTIFIERS_PATH, calculator.getRelativeQuantifiers());
        serializationService.save(ABSOLUTE_QUANTIFIERS_PATH, calculator.getAbsoluteQuantifiers());
    }

}
