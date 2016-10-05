package org.eljaiek.proxy.select.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author eljaiek
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DProxy implements Serializable {

    private String name;

    private String host;

    private int port;

    @JsonIgnore
    public String getHostPort() {
        return host.concat(":").concat(String.valueOf(port));
    }
}
