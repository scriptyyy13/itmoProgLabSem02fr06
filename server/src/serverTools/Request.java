package serverTools;

import commands.CommandRequest;

import java.net.SocketAddress;

public record Request(SocketAddress client, CommandRequest command){}
