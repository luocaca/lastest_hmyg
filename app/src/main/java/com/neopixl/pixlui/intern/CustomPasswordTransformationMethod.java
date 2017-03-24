/*
 Copyright 2013 Neopixl

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this

file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under

the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF 

ANY KIND, either express or implied. See the License for the specific language governing

permissions and limitations under the License.
 */
package com.neopixl.pixlui.intern;

import android.text.method.PasswordTransformationMethod;
import android.view.View;

public class CustomPasswordTransformationMethod extends
PasswordTransformationMethod {
	@Override
	public CharSequence getTransformation(CharSequence source, View view) {
		return new CustomPasswordCharSequence(source);
	}

	private class CustomPasswordCharSequence implements CharSequence {

		private CharSequence mSource;

		public CustomPasswordCharSequence(CharSequence source) {
			setSource(source);
		}

		public char charAt(int index) {
			return (char) 42;
		}

		public int length() {
			return getSource().length();
		}

		public CharSequence subSequence(int start, int end) {
			return getSource().subSequence(start, end);
		}

		private CharSequence getSource() {
			return mSource;
		}

		private void setSource(CharSequence mSource) {
			this.mSource = mSource;
		}
	}
};
/*
0 value:锟斤拷
33 value:!
34 value:"
35 value:#
36 value:$
37 value:%
38 value:&
39 value:'
40 value:(
41 value:)
42 value:*
43 value:+
44 value:,
45 value:-
46 value:.
47 value:/
48 value:0
49 value:1
50 value:2
51 value:3
52 value:4
53 value:5
54 value:6
55 value:7
56 value:8
57 value:9
58 value::
59 value:;
60 value:<
61 value:=
62 value:>
63 value:?
64 value:@
65 value:A
66 value:B
67 value:C
68 value:D
69 value:E
70 value:F
71 value:G
72 value:H
73 value:I
74 value:J
75 value:K
76 value:L
77 value:M
78 value:N
79 value:O
80 value:P
81 value:Q
82 value:R
83 value:S
84 value:T
85 value:U
86 value:V
87 value:W
88 value:X
89 value:Y
90 value:Z
91 value:[
92 value:\
93 value:]
94 value:^
95 value:_
96 value:`
97 value:a
98 value:b
99 value:c
100 value:d
101 value:e
102 value:f
103 value:g
104 value:h
105 value:i
106 value:j
107 value:k
108 value:l
109 value:m
110 value:n
111 value:o
112 value:p
113 value:q
114 value:r
115 value:s
116 value:t
117 value:u
118 value:v
119 value:w
120 value:x
121 value:y
122 value:z
123 value:{
124 value:|
125 value:}
126 value:~
161 value:隆
162 value:垄
163 value:拢
164 value:陇
165 value:楼
166 value:娄
167 value:搂
168 value:篓
169 value:漏
170 value:陋
171 value:芦
172 value:卢
173 value:颅
174 value:庐
175 value:炉
176 value:掳
177 value:卤
178 value:虏
179 value:鲁
180 value:麓
181 value:碌
182 value:露
183 value:路
184 value:赂
185 value:鹿
186 value:潞
187 value:禄
188 value:录
189 value:陆
190 value:戮
191 value:驴
192 value:脌
193 value:脕
194 value:脗
195 value:脙
196 value:脛
197 value:脜
198 value:脝
199 value:脟
200 value:脠
201 value:脡
202 value:脢
203 value:脣
204 value:脤
205 value:脥
206 value:脦
207 value:脧
208 value:脨
209 value:脩
210 value:脪
211 value:脫
212 value:脭
213 value:脮
214 value:脰
215 value:脳
216 value:脴
217 value:脵
218 value:脷
219 value:脹
220 value:脺
221 value:脻
222 value:脼
223 value:脽
224 value:脿
225 value:谩
226 value:芒
227 value:茫
228 value:盲
229 value:氓
230 value:忙
231 value:莽
232 value:猫
233 value:茅
234 value:锚
235 value:毛
236 value:矛
237 value:铆
238 value:卯
239 value:茂
240 value:冒
241 value:帽
242 value:貌
243 value:贸
244 value:么
245 value:玫
246 value:枚
247 value:梅
248 value:酶
249 value:霉
250 value:煤
251 value:没
252 value:眉
253 value:媒
254 value:镁
255 value:每
 */