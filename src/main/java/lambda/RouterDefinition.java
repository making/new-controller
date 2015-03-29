package lambda;

import me.geso.routes.WebRouter;

public interface RouterDefinition<T> {
    void define(WebRouter<T> router);
}
