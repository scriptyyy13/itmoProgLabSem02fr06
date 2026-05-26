package serverTools;

import commands.CommandRequest;
import sharedTools.Message;

import java.net.SocketAddress;

public record ResultOfRequest(SocketAddress client, Message answer){}
