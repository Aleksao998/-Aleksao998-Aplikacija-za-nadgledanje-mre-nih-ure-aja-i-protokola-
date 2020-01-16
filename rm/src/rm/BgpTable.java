package rm;

import java.io.IOException;


import com.ireasoning.protocol.snmp.*;

public class BgpTable {
	//Atributes
	private String ipAddress, rdCommunity, wrCommunity;
	private int portNumber;
	private SnmpTarget target;
	private SnmpSession session;
	//Methods
	public BgpTable(String ip) throws IOException {
		ipAddress=ip;
		rdCommunity=wrCommunity="si2019";
		portNumber=161;
		target = new SnmpTarget(ipAddress,portNumber,rdCommunity,wrCommunity);
		session = new SnmpSession(target);
	}
	public int getLength() throws IOException {
		return getOriginAtt().length;
	}
	public SnmpVarBind[] getOriginAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.4");
	}
	public SnmpVarBind[] getAsPathAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.5");
	}
	public SnmpVarBind[] getNextHopAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.6");
	}
	public SnmpVarBind[] getMedAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.7");
	}
	public SnmpVarBind[] getLpAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.8");
	}
	public SnmpVarBind[] getAtomicAggregateAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.9");
	}
	public SnmpVarBind[] getAggregatorAsAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.10");
	}
	public SnmpVarBind[] getAggregatorAddrAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.11");
	}
	public SnmpVarBind[] getBestRouteAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.13");
	}
	public SnmpVarBind[] getNetworksAtt() throws IOException{
		return session.snmpGetTableColumn(".1.3.6.1.2.1.15.6.1.3");
	}
	
	
}
