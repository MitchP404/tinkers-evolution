package xyz.phanta.tconevo.integration.solarflux;

import xyz.phanta.tconevo.integration.IntegrationHooks;
import xyz.phanta.tconevo.util.Reflected;

import java.util.stream.Stream;

public interface SolarFluxHooks extends IntegrationHooks {

    String MOD_ID = "solarflux";

    @Inject(MOD_ID)
    SolarFluxHooks INSTANCE = new Noop();

    Stream<SolarCellData> getSolarTypes();

    @Reflected
    class Noop implements SolarFluxHooks {

        @Override
        public Stream<SolarCellData> getSolarTypes() {
            return Stream.empty();
        }

    }

}
