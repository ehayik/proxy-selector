
package org.eljaiek.proxy.select.controller;

import org.eljaiek.proxy.select.services.ProxyDetails;
import org.mapstruct.Mapper;

/**
 *
 * @author eduardo.eljaiek
 */
@Mapper(componentModel = "spring")
public interface ProxyModelMapper {
    
    ProxyModel asProxyModel(ProxyDetails proxy);
    
    ProxyDetails  asProxyDetails(ProxyModel proxyModel);     
}
