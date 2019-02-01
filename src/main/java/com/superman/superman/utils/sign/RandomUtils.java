/**
 * 
 */
package com.superman.superman.utils.sign;

import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**  
* @Description: TODO(随机数工具类)
* @author heguoliang  
* @date 2016年10月10日 上午8:50:13
*/
public class RandomUtils {

	private static final Random RANDOM=new Random();

	public static Random getRandom() {
		return RANDOM;
	}

	private static char[] chars = {
			'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n',
			'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z',
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			'A', 'B', 'C', 'D', 'E', 'F', 'G',
			'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z'
	};

	/**
	 * 随机生成由0-9a-zA-Z组合而成的字符串
	 *
	 * @param len 字符串长度
	 * @return 生成结果
	 */
	public static String randomChar(int len) {
		StringBuilder shortBuffer = new StringBuilder();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < len; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

	/**
	 * 随机数+日期
	 * @param startaKey
	 * @param endKey
	 * @return
	 */
	public static String randomKey(String startaKey, String endKey) {
		String simpleDate = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		String result = simpleDate + randomSixNum();
		if (StringUtils.isNotBlank(startaKey)) {
			result = startaKey + result;
		}
		if (StringUtils.isNotBlank(endKey)) {
			result = result + endKey;
		}
		return result;
	}
	
	public static Integer randomKey(int count) {
		Integer result = getRandom().nextInt(count);
		return result + 1;
	}

	/**
	 * 随机生成六位随机数
	 * @return
	 */
	public static int randomSixNum() {
		int randomNum = (int) ((Math.random() * 9 + 1) * 100000);
		return randomNum;
	}

}
