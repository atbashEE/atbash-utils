/*
 * Copyright 2014-2020 Rudy De Busscher (https://www.atbash.be)
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
package be.atbash.util;

import be.atbash.util.exception.AtbashIllegalActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.el.ValueExpression;
import jakarta.faces.component.EditableValueHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.ValueHolder;
import jakarta.faces.component.html.HtmlInputText;
import jakarta.faces.component.html.HtmlOutputLabel;
import jakarta.faces.component.html.HtmlSelectOneMenu;
import jakarta.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.List;

/**
 * Some utility methods related too JSF components.
 */
@PublicAPI
public final class ComponentUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentUtils.class);

    private ComponentUtils() {
    }

    /**
     * Return the {@code value} from the component ({@link ValueHolder}). It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'value' attribute of the JSF Component
     */
    public static Object getValue(UIComponent component, FacesContext facesContext) {
        if (component == null) {
            throw new IllegalArgumentException("component parameter is required");
        }
        ValueExpression value = component.getValueExpression("value");
        if (value == null) {
            if (component instanceof ValueHolder) {

                return ((ValueHolder) component).getValue();
            } else {
                return null;
            }
        } else {
            return value.getValue(facesContext.getELContext());
        }
    }

    /**
     * Return the {@code required} info from the component ({@link EditableValueHolder}). It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'required' attribute of the JSF Component
     */
    public static boolean isRequired(UIComponent component, FacesContext facesContext) {
        if (component == null) {
            throw new IllegalArgumentException("component parameter is required");
        }
        ValueExpression value = component.getValueExpression("required");
        if (value == null) {
            if (component instanceof EditableValueHolder) {

                return ((EditableValueHolder) component).isRequired();
            } else {
                return false; // default value when no attribute is specified
            }
        } else {
            Object result = value.getValue(facesContext.getELContext());
            if (result != null) {
                return (Boolean) result;
            }
            return false; // default value when no attribute is specified
        }
    }

    /**
     * Return the {@code style} from the component ({@link HtmlInputText} or {@link HtmlOutputLabel}). It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'style' attribute of the JSF Component
     */
    // FIXME There are so many different classes with getStyle Method, reflection?!
    public static String getStyle(UIComponent component, FacesContext facesContext) {
        if (component == null) {
            throw new IllegalArgumentException("component parameter is required");
        }
        ValueExpression value = component.getValueExpression("style");
        if (value == null) {
            if (component instanceof HtmlInputText) {

                return ((HtmlInputText) component).getStyle();
            }
            if (component instanceof HtmlSelectOneMenu) {

                return ((HtmlSelectOneMenu) component).getStyle();
            }

            if (component instanceof HtmlOutputLabel) {
                return ((HtmlOutputLabel) component).getStyle();
            }

            return null;

        } else {
            return value.getValue(facesContext.getELContext()).toString();
        }
    }

    /**
     * Return the {@code styleClass} from the component ({@link HtmlInputText}, {@link HtmlOutputLabel} or {@link HtmlSelectOneMenu}). It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'styleClass' attribute of the JSF Component
     */
    // FIXME There are so many different classes with getStyle Method, reflection?!
    public static String getStyleClass(UIComponent component, FacesContext facesContext) {
        if (component == null) {
            throw new IllegalArgumentException("component parameter is required");
        }
        ValueExpression value = component.getValueExpression("styleClass");
        if (value == null) {
            if (component instanceof HtmlInputText) {

                return ((HtmlInputText) component).getStyleClass();
            }
            if (component instanceof HtmlSelectOneMenu) {

                return ((HtmlSelectOneMenu) component).getStyleClass();
            }
            if (component instanceof HtmlOutputLabel) {
                return ((HtmlOutputLabel) component).getStyleClass();
            }

            return null;

        } else {
            return value.getValue(facesContext.getELContext()).toString();
        }
    }

    /**
     * Return the {@code maxLength} from the component ({@link HtmlInputText}. It checks first the valueExpression, if not
     * present then the getter is executed for retrieving fixed values.
     *
     * @param component    JSF component to get the value from.
     * @param facesContext The FacesContext
     * @return The 'maxLength' attribute of the JSF Component
     */
    public static int getMaxLength(UIComponent component, FacesContext facesContext) {
        if (component == null) {
            throw new IllegalArgumentException("component parameter is required");
        }
        ValueExpression value = component.getValueExpression("maxlength");
        if (value == null) {
            if (component instanceof HtmlInputText) {
                return ((HtmlInputText) component).getMaxlength();
            }
            return Integer.MIN_VALUE;
        }
        return Integer.parseInt(value.getValue(facesContext.getELContext()).toString());

    }

    /**
     * Returns the attribute value of a custom component. The attribute name is defined as parameter of the method, just as the type.
     * The method looks for a 'static' hardcoded value and an EL Value expression. In the latter case, the expression is evaluated.
     *
     * @param component     The custom UIComponent.
     * @param attributeName attribute name
     * @param resultClass   The class type of the attribute used for casting
     * @param <T>           The generic type.
     * @return The value of the attribute of
     */
    public static <T> T getAttributeValue(UIComponent component, String attributeName, Class<T> resultClass) {
        if (component == null) {
            throw new IllegalArgumentException("component parameter is required");
        }
        T result = null;

        if (component.getAttributes().containsKey(attributeName)) {
            if (Boolean.class.equals(resultClass)) {
                // He, maybe it should be possible to store the correct type in the attributes.  We specified it in
                // the taglib.xml
                result = (T) Boolean.valueOf((String) component.getAttributes().get(attributeName));
            } else {
                result = (T) component.getAttributes().get(attributeName);
            }
        }
        if (result == null) {
            ValueExpression ve = component.getValueExpression(attributeName);
            if (ve != null) {
                result = (T) ve.getValue(FacesContext.getCurrentInstance().getELContext());
            }
        }
        return result;
    }

    /**
     * Searches for JSF Components within the UIComponent or his parents in the component tree. It is thus an
     * extended version of the algorithm than the standard JSF algorithm <code>UIComponent.findComponent()</code>
     * but comes with a potential performance. The algorithm is as follows.
     * <p/>
     * <ul>
     * <li>When target is empty, it returns the UIComponent itself</li>
     * <li>the target is split around , and the search is performed for each id</li>
     * <li>The id is search with the parent with a call to <code>UIComponent.findComponent</code>. Thus absolute ids are supported</li>
     * <li>When no match is found, the search is repeated on the parent component (and this partially overlaps the previous search)</li>
     * </ul>
     *
     * @param component The Component where the search starts.
     * @param target    The id or set of ids (separated by ,) to be searched. Absolute ids (like :frm:field) are supported.
     * @return List of Component matching the search ids.
     */
    public static List<UIComponent> findTargets(UIComponent component, String target) {
        if (component == null) {
            throw new IllegalArgumentException("component parameter is required");
        }

        List<UIComponent> result = new ArrayList<>();
        if (StringUtils.isEmpty(target)) {
            result.add(component);
        } else {
            UIComponent targetComponent;
            String[] targetIds = target.split(",");
            for (String targetId : targetIds) {
                String id = targetId.trim();
                if (id.contains(" ")) {
                    throw new AtbashIllegalActionException(String.format("(JSF-DEV-01) search id '%s' is invalid", id));
                }
                targetComponent = lookupComponentInTree(component, id);
                if (targetComponent != null) {
                    result.add(targetComponent);
                } else {
                    if (LOGGER.isWarnEnabled()) {
                        LOGGER.warn(String.format("Unable to find component with ID %s in view.", targetId));
                    }
                }
            }
        }
        return result;
    }

    /**
     * Searches for JSF Components within the UIComponent or his parents in the component tree. It is thus an
     * extended version of the algorithm than the standard JSF algorithm <code>UIComponent.findComponent()</code>
     * but comes with a potential performance. For each component found, the callback is called.
     * <p/>
     * <ul>
     * <li>When target is empty, call callback method </li>
     * <li>the target is split around , and the search is performed for each id</li>
     * <li>The id is search with the parent with a call to <code>UIComponent.findComponent</code>. Thus absolute ids are supported</li>
     * <li>When no match is found, the search is repeated on the parent component (and this partially overlaps the previous search)</li>
     * </ul>
     * For each target found, the callback is called. the detection is based on the fact the the parent of the component needs the attribute <code>javax.faces.application.Resource.ComponentResource</code>
     *
     * @param component The Component where the search starts.
     * @param target    The id or set of ids (separated by ,) to be searched. Absolute ids (like :frm:field) are supported.
     * @param callback  Process the found components.
     */
    public static void processTargets(UIComponent component, String target, ComponentCallback callback) {
        // FIXME Review
        if (callback == null) {
            throw new IllegalArgumentException("callback parameter is required");
        }
        if (StringUtils.isEmpty(target)) {
            callback.handle(component, false);
            // TODO is false parameter correctly? should we verify it?
        } else {
            UIComponent targetComponent;
            String[] targets = target.split(",");
            for (String targetId : targets) {
                String id = targetId.trim();
                if (id.contains(" ")) {
                    throw new AtbashIllegalActionException(String.format("(JSF-DEV-01) search id '%s' is invalid (spaces, ...)", id));
                }
                targetComponent = lookupComponentInTree(component, id);
                if (targetComponent != null) {
                    callback.handle(targetComponent, false);
                } else {
                    if (isInCustomComponent(component)) {
                        callback.handle(component, true);
                    } else {
                        if (LOGGER.isWarnEnabled()) {
                            LOGGER.warn(String.format("Unable to find component with ID %s in view.", targetId));
                        }
                    }
                }
            }
        }

    }

    private static UIComponent lookupComponentInTree(UIComponent parent, String targetId) {
        UIComponent targetComponent = null;
        if (parent != null) {
            try {
                targetComponent = parent.findComponent(targetId);
            } catch (IllegalArgumentException e) {
                throw new AtbashIllegalActionException(String.format("(JSF-DEV-01) search id '%s' references invalid components (intermediates no naming container)", targetId));
            }
            if (targetComponent == null) {
                return lookupComponentInTree(parent.getParent(), targetId);
            }
        }
        return targetComponent;
    }

    private static boolean isInCustomComponent(UIComponent uiComponent) {
        boolean result = false;
        if (uiComponent != null && uiComponent.getParent() != null && uiComponent.getParent().getAttributes() != null) {
            result = uiComponent.getParent().getAttributes()
                    .containsKey("javax.faces.application.Resource.ComponentResource");
        }
        return result;
    }
}
