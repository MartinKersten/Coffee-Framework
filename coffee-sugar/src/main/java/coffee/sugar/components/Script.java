package coffee.sugar.components;

import java.io.IOException;

import coffee.core.util.Util;
import coffee.sugar.Component;

public class Script extends Component {

	private String src;

	@Override
	public void configure() {
		if (!Util.isNull(src))
			registerScriptInclude(src);
		else
			registerScriptBlock(getTextContent());
	}

	@Override
	public void render() throws IOException {}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getSrc() {
		return src;
	}

}
