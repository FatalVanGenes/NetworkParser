package xml.parser;

import org.junit.Test;

import xml.core.model.Network;


public class TestParser {

	public TestParser() {
		// TODO Auto-generated constructor stub
	}

	// @Test
	public void testParser() throws Exception {
		String fileName = "/home/mrsteve/Desktop/Home Network/StaX/opnet_test_3MB.xml";
		
		Network network = NetworkParser.getNetworkFromXML(fileName);
		System.out.println(network);
	}
	
	@Test
	public void testWriter() throws Exception {
		String inputFileName = "/home/mrsteve/Desktop/Home Network/StaX/opnet_test_3MB.xml";
		String outputFileName = "/home/mrsteve/Desktop/Home Network/StaX/test_output.xml";
		
		NetworkParser.mergeAndWriteXML(inputFileName, outputFileName, null);
	}
}
