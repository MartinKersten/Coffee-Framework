/**
 * Copyright 2011 Miere Liniel Teixeira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package coffee.components;

import java.io.IOException;
import java.util.List;

import coffee.binding.Evaluator;
import coffee.core.CoffeeContext;
import coffee.core.IResource;


/**
 * Default component interface. Here are defined the minimum methods to
 * the component works well on Coffee Life Cycle.
 * 
 * @author Miere Liniel Teixeira
 */
public interface IComponent extends Cloneable {

/**
 * Configures the component.
 * 
 * This is a useful method to be override, configure attributes, add custom
 * child elements and change the default component behavior.
 */
	public void configure();

/**
 * Renders the component into a HTML document.
 * @throws IOException 
 */
	public void render() throws IOException;

/**
 * Clones the component.
 * 
 * Once the resource template is parsed, the component Stack Tree is generated.
 * By default, if a template was already parsed the Coffee Life Cycle doesn't
 * parses it again, it clones the object to improve the parse performance.<br/>
 * <br/>
 * Note: this method is handled by the Coffee Life Cycle and should not be called
 * unless you really knows what are you doing.
 * 
 * @return
 * @throws CloneNotSupportedException
 */
	public Object clone() throws CloneNotSupportedException;

/**
 * Appends a child at end of children's list.
 * @param child
 */
	public void addChild(IComponent child);

/**
 * Retrieves the number of children,
 * @return
 */
	public int getNumChildren();

/**
 * Set a new list of component to use as children of the component.
 * @param value
 */
	public void setChildren(List<IComponent> value);

/**
 * Retrieves the children's list.
 * @return
 */
	public List<IComponent> getChildren();

/**
 * Set text content of the component. Some components is designed to
 * handle the markup's <i>default value</i> of the tag (<code><i>&lt;ns:component&gt;Here
 * goes the default value&lt;/ns:component&gt;</i></code>). By default this method
 * should stores the <i>default value</i> into a String field inside of component.
 * 
 * @param content
 */
	public void setTextContent(String content);

/**
 * Retrieves the text content of the component.
 * @return the text content string.
 * @see IComponent#setTextContent(String)
 */
	public String getTextContent();

/**
 * Retrieves an attribute value from the component.
 * @param attribute
 * @return
 */
	public Object getAttribute(String attribute);

/**
 * Set's a value to the component attribute.
 * @param attribute
 * @param value
 */
	public void setAttribute(String attribute, Object value);

/**
 * Retrieves the parent component.
 * @return
 */
	public IComponent getParent();

/**
 * Set's the parent component.
 * @param parent
 */
	public void setParent(IComponent parent);

/**
 * Returns if the component is a <i>Value Holder Component</i>.<br/>
 * <br/>
 * Some components like TextInput, ComboBox'es, Checkbox'es, etc. Stores values
 * written from the end user. This components, when posted to the server, holds
 * the user value which can be later handled by the <i>Resource Hook</i> or
 * binded to an object with <i>Simple Value Expressions</i>.
 * 
 * @return true if it is a <i>Value Holder Component</i>, otherwise, false.
 * @see IResource
 * @see Evaluator
 */
	public boolean isValueHolder();

/**
 * Get the component's unique identification. By default, the main implementation
 * of component generates a sequential id's for null component's id at the render
 * time and is usually stored at <i>'id'</i> HTML attribute.
 * @return
 * @see AbstractComponent
 */
	public String getId();

/**
 * Set the component's unique identification.
 * @param id
 */
	public void setId(String id);

/**
 * Get's the default value for the component. This attribute is usually used to holds
 * informations retrieved by the user on <i>Value Holder Component's</i>.
 * 
 * @return
 * @see IComponent#isValueHolder
 */
	public Object getHeldValue();

/**
 * Get's the default value for the component.
 * @param value
 * @see IComponent#getHoldenValue()
 */
	public void holdValue(String value);

/**
 * Sets the current CoffeeContext.
 */
	public void setCoffeeContext(CoffeeContext context);
	
/**
 * Retrieves the current CoffeeContext
 */
	public CoffeeContext getCoffeeContext();

/**
 * Defines the component name. It is usually set by the {@link IComponentFactory#newComponent(String)}
 * method at the correct component instantiation.
 * 
 * @param name
 */
	void setComponentName(String name);
	
/**
 * Retrieves the component name.
 * @return 
 */
	public String getComponentName();

/**
 * Apply the bind process to the component. It usually bind the value stored at
 * the component, if it is a <i>Value Holder Component</i>, and update the target
 * object.<br/>
 * <br/>
 * Note: this method is handled by the Coffee Life Cycle and should not be called
 * unless you really knows what are you doing.
 */
	public void bind();

/**
 * Flushes this component and forces any <i>Value Holder Component</i> data to
 * be configured, and if needed, re-binded against its target. The general contract
 * of flush is when calling it, if any <i>Value Holder Component</i> child is present at
 * <i>Component Tree</i>, it will be flushed in cascade.<br/>
 * <br/>
 * Note: this method is handled by the Coffee Life Cycle and should not be called
 * unless you really knows what are you doing.
 */
	public void flush();
}
