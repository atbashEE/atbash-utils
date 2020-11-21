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
package be.atbash.util.jsf;

import jakarta.faces.application.Application;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseStream;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.RenderKit;
import java.util.*;

public class FakeFacesContext extends FacesContext {

    private ExternalContext externalContext;
    private Application application;
    private Map<String, List<FacesMessage>> messages = new HashMap<>();
    private ResponseStream responseStream;
    private ResponseWriter responseWriter;

    private FakeFacesContext() {
    }

    private FakeFacesContext(ExternalContext externalContext) {
        this.externalContext = externalContext;
    }

    private FakeFacesContext(Application application) {
        this.application = application;
    }

    private FakeFacesContext(Application application, ExternalContext externalContext) {
        this.externalContext = externalContext;
        this.application = application;
    }

    @Override
    public Application getApplication() {
        return application;
    }

    @Override
    public Iterator<String> getClientIdsWithMessages() {
        return messages.keySet().iterator();
    }

    @Override
    public ExternalContext getExternalContext() {
        return externalContext;
    }

    @Override
    public FacesMessage.Severity getMaximumSeverity() {
        FacesMessage.Severity result = null;
        for (List<FacesMessage> items : messages.values()) {
            for (FacesMessage facesMessage : items) {
                if (result == null) {
                    result = facesMessage.getSeverity();
                } else {
                    if (facesMessage.getSeverity().compareTo(result) > 0) {
                        result = facesMessage.getSeverity();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Iterator<FacesMessage> getMessages() {
        List<FacesMessage> result = new ArrayList<>();
        for (List<FacesMessage> entry : messages.values()) {
            result.addAll(entry);
        }
        return result.iterator();
    }

    @Override
    public Iterator<FacesMessage> getMessages(String clientId) {
        List<FacesMessage> result = messages.get(clientId);
        if (result == null) {
            result = new ArrayList<>();
        }
        return result.iterator();
    }

    @Override
    public RenderKit getRenderKit() {
        return null;
    }

    @Override
    public boolean getRenderResponse() {
        return false;
    }

    @Override
    public boolean getResponseComplete() {
        return false;
    }

    @Override
    public ResponseStream getResponseStream() {
        return responseStream;
    }

    @Override
    public void setResponseStream(ResponseStream responseStream) {

        this.responseStream = responseStream;
    }

    @Override
    public ResponseWriter getResponseWriter() {
        return responseWriter;
    }

    @Override
    public void setResponseWriter(ResponseWriter responseWriter) {

        this.responseWriter = responseWriter;
    }

    @Override
    public UIViewRoot getViewRoot() {
        return null;
    }

    @Override
    public void setViewRoot(UIViewRoot root) {

    }

    @Override
    public void addMessage(String clientId, FacesMessage message) {
        List<FacesMessage> item = messages.computeIfAbsent(clientId, k -> new ArrayList<>());
        item.add(message);
    }

    @Override
    public void release() {

    }

    @Override
    public void renderResponse() {

    }

    @Override
    public void responseComplete() {

    }

    public static void registerFake() {
        FacesContext.setCurrentInstance(new FakeFacesContext());
    }

    public static void registerFake(ExternalContext externalContext) {
        FacesContext.setCurrentInstance(new FakeFacesContext(externalContext));
    }

    public static void registerFake(Application application) {
        FacesContext.setCurrentInstance(new FakeFacesContext(application));
    }

    public static void registerFake(Application application, ExternalContext externalContext) {
        FacesContext.setCurrentInstance(new FakeFacesContext(application, externalContext));
    }
}
