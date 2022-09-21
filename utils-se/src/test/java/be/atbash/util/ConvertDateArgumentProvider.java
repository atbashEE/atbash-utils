/*
 * Copyright 2014-2022 Rudy De Busscher (https://www.atbash.be)
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

import be.atbash.util.exception.ConvertException;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class ConvertDateArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
        return Stream.of(
                // Year month day
                Arguments.of(ConvertDateArgument.of("2022", 2022, 1, 1, 1, 0, 0))  // 1
                , Arguments.of(ConvertDateArgument.of("2022/3", 2022, 3, 1, 1, 0, 0))  // 2
                , Arguments.of(ConvertDateArgument.of("2022 march", 2022, 3, 1, 1, 0, 0))  // 3
                , Arguments.of(ConvertDateArgument.of("2022/4/23", 2022, 4, 23, 1, 0, 0))  // 4
                , Arguments.of(ConvertDateArgument.of("2022/4/23 15", 2022, 4, 23, 15, 0, 0))  // 5
                , Arguments.of(ConvertDateArgument.of("2022/4/23T17", 2022, 4, 23, 17, 0, 0))  // 6
                , Arguments.of(ConvertDateArgument.of("2022/4/23 17:2", 2022, 4, 23, 17, 2, 0))  // 7
                , Arguments.of(ConvertDateArgument.of("2022/4/23 17:2:15", 2022, 4, 23, 17, 2, 15))  // 8
                , Arguments.of(ConvertDateArgument.of("2022/4/23 5:02:15 pm", 2022, 4, 23, 17, 2, 15))  // 9
                // month day year
                , Arguments.of(ConvertDateArgument.of("mei 15 2022", 2022, 5, 15, 0, 0, 0))  // 10
                , Arguments.of(ConvertDateArgument.of("dec 15 2022", 2022, 12, 15, 0, 0, 0))  // 11
                , Arguments.of(ConvertDateArgument.of("mei 15 2022 17:2:15", 2022, 5, 15, 17, 2, 15))  // 12
                , Arguments.of(ConvertDateArgument.of("mei 15 2022 15", 2022, 5, 15, 15, 0, 0))  // 13
                , Arguments.of(ConvertDateArgument.of("mei 15 2022 3 a.m.", 2022, 5, 15, 3, 0, 0))  // 14
                , Arguments.of(ConvertDateArgument.of("mei 15 2022 3:1 p.m.", 2022, 5, 15, 15, 1, 0))  // 15
                // day month year
                , Arguments.of(ConvertDateArgument.of("21 9 2022", 2022, 9, 21, 0, 0, 0))  // 16
                , Arguments.of(ConvertDateArgument.of("21 september 2022 21", 2022, 9, 21, 21, 0, 0))  // 17
                // Incorrect values
                , Arguments.of(ConvertDateArgument.of("2022 xyz", ConvertException.class, "can not parse xyz as month"))  // 18
        );
    }
}
