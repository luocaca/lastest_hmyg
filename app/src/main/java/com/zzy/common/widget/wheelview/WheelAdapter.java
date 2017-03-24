/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.zzy.common.widget.wheelview;

public interface WheelAdapter {
	
	/** 获取条目总数 */
    public abstract int getItemsCount();

    /** 获取条目ID */
    public abstract int getItemId(int index);

    /** 获取条目内容 */
    public abstract String getItem(int index);

    /** 获取条目内容最长长度 */
    public abstract int getMaximumLength();
}
