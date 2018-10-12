package xml.core.model;

abstract class AbstractElement implements Element {

	private final String typeName;
	
	private int renderIndentLevel = -1;
	
	AbstractElement(String typeName) {
		this.typeName = typeName;
	}

	@Override
	public String getTypeName() {
		return typeName;
	}

	@Override
	public int getRenderIndentLevel() {
		return renderIndentLevel;
	}

	@Override
	public void setRenderIndentLevel(int indentLevel) {
		this.renderIndentLevel = indentLevel;
	}
}
