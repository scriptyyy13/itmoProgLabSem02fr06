package serverTools;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

public record ByteRequest(ByteBuffer bytes, SocketAddress client) {
}
