package coffee.components;

import static coffee.util.Util.isNull;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import coffee.binding.CoffeeBinder;
import coffee.core.CoffeeContext;
import coffee.util.Reflection;
import coffee.util.Util;

/**
 * Default implementation for components. Software developers can override
 * the methods for a custom component development. The <b><i>See Also</i></b>
 * section has useful methods JavaDocs to understand the basics for construct
 * your self custom component.
 * 
 * @author Miere Liniel Teixeira
 * @see IComponent#clone()
 * @see IComponent#getChildren()
 * @see IComponent#getId()
 * @see IComponent#getTextContent()
 * @see IComponent#render()
 */
public abstract class AbstractComponent implements IComponent {

	protected List<IComponent> childs;
	protected Map<String, Object> attributes;
	private IComponent parent;
	private String textContent;
	private String id;
	private Object value;
	protected CoffeeContext coffeeContext;
	private String componentName;

	public AbstractComponent() {
		super();
		childs = new ArrayList<IComponent>();
		attributes = new HashMap<String, Object>();
	}

	@Override
	public abstract void configure();

	public abstract void render() throws IOException;

	/**
	 * 
	 * @param attr
	 * @return
	 */
	public String getAttributeValue(String attr) {
		Object object = attributes.get(attr);
		if (isNull(object))
			return null;
		
		Object value = CoffeeBinder.getValue(object.toString(), getCoffeeContext());
		if (isNull(value))
			return "";

		return value.toString();
	}

	@Override
	public void bind() {
		configure();

		if (!isValueHolder())
			return;

		String expression = (String)getHeldValue();
		String value = coffeeContext.getRequest().getParameter(getId());
		CoffeeBinder.setValue(expression, coffeeContext, value);
	}

	@Override
	public void flush() {
		bind();
	
		// Flushes child elements in cascade
		for (IComponent child : childs) {
			child.setCoffeeContext(coffeeContext);
			child.flush();
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		AbstractComponent clone = (AbstractComponent)super.clone();
		List<IComponent> children = new ArrayList<IComponent>();
		
		for (IComponent child : childs) {
			child.setParent(clone);
			children.add((IComponent)child.clone());
		}
		
		clone.setChildren(children);
		clone.setTextContent(textContent);
		return clone;
	}

	@Override
	public void addChild(IComponent child) {
		childs.add(child);
	}

/**
 * Insert a child on the children list at position defined by <i>index</i> parameter.
 * @param child
 * @param index
 */
	public void addChild(IComponent child, int index) {
		childs.add(index, child);
	}

	@Override
	public void setTextContent(String content) {
		this.textContent = content;
	}

	public String getTextContent() {
		return this.textContent;
	}

	@Override
/**
 * Retrieves the attribute value from component as Object.<br/>
 * <br/>
 * If the component implementation has a setter method with same name of the attribute
 * it will be dispatched and ignoring the value binding. Otherwise, it will try to
 * retrieve the value from a possible defined binding expression.
 * 
 * @param attribute
 */
	public Object getAttribute(String attribute) {
		try{
			if (attribute.equals("class"))
				return getAttributeValue(attribute);
			Method getter = Reflection.extractGetterFor(attribute, this);
			return getter.invoke(this);
		} catch (Exception e) {
			return getAttributeValue(attribute);
		}
	}

	@Override
/**
 * Sets an attribute value.<br/>
 * <br/>
 * After stores the value, if the component implementation has a setter method with same name of the attribute
 * it will be dispatched too.
 */
	public void setAttribute(String attribute, Object value) {
		this.attributes.put(attribute, value);
		try {
			Method setter = Reflection.extractSetterFor(attribute, this, value);
			setter.invoke(this, value);
		} catch (Exception e) {
			return;
		}
	}

	@Override
	public IComponent getParent() {
		return parent;
	}

	@Override
	public void setParent(IComponent parent) {
		this.parent = parent;
	}

	@Override
	public int getNumChildren() {
		return childs.size();
	}

	@Override
	public List<IComponent> getChildren() {
		return childs;
	}

	@Override
	public void setChildren(List<IComponent> value) {
		this.childs = value;
	}

	@Override
	public boolean isValueHolder() {
		return !Util.isNull(getHeldValue());
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public Object getHeldValue() {
		return value;
	}

	@Override
	public void holdValue(String value) {
		this.value = value;
	};

	@Override
	public void setCoffeeContext(CoffeeContext context) {
		this.coffeeContext = context;
	}

	@Override
	public CoffeeContext getCoffeeContext() {
		return this.coffeeContext;
	}

	@Override
	public void setComponentName(String name) {
		this.componentName = name;
	}

	@Override
	public String getComponentName() {
		return this.componentName;
	}

}