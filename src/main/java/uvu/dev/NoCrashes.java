package uvu.dev;

import net.fabricmc.api.ModInitializer;
import org.slf4j.*;

public class NoCrashes implements ModInitializer
{
    public static final Logger LOGGER = LoggerFactory.getLogger("nocrashes");

    public void onInitialize()
    {
        LOGGER.info("NoCrashes initialized.");
    }
}
