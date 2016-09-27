
package org.eljaiek.proxy.select.model;

import fr.xebia.extras.selma.Mapper;
import org.eljaiek.proxy.select.domain.DProxy;

/**
 *
 * @author eduardo.eljaiek
 */
@Mapper( withIgnoreFields="hostport" )
public interface ProxyModelMapper {
    
    ProxyModel asProxyModel(DProxy proxy);
    
    DProxy  asDProxy(ProxyModel proxyModel);     
}
