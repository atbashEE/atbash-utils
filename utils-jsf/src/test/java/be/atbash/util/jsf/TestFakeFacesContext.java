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

import org.junit.jupiter.api.Test;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import static org.assertj.core.api.Assertions.assertThat;

public class TestFakeFacesContext {

    @Test
    public void getMaximumSeverity() {
        FakeFacesContext.registerFake();

        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.addMessage("test1", new FacesMessage(FacesMessage.SEVERITY_INFO, "summary1", "detail1"));
        facesContext.addMessage("test2", new FacesMessage(FacesMessage.SEVERITY_FATAL, "summary2", "detail2"));
        facesContext.addMessage("test2", new FacesMessage(FacesMessage.SEVERITY_WARN, "summary3", "detail3"));

        assertThat(facesContext.getMaximumSeverity()).isEqualTo(FacesMessage.SEVERITY_FATAL);
    }
}
