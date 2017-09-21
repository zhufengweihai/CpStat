package com.zf.lottery.update;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.zf.lottery.common.Commons;
import com.zf.lottery.dao.LotteryManager;
import com.zf.lottery.data.Lottery;

public class LotteryRequestServiceImpl implements LotteryRequestService {
	private static final String COUNT_END = "Ìõ¼ÇÂ¼";
	private static final String COUNT_START = "<div class=\"page\" id=\"pages\">";
	private static final String RESULT_END = "</tr>";
	private static final String RESULT_START = "<td><span class=\"c_333\">";
	private static final int BASE_TIME = 10000;
	private static final String URL = "http://www.caipiaow.com/index.php?m=kaijiang&a=index&cz=cq&type=ssc&p=1";
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private Pattern pattern = Pattern.compile("[^0-9]");

	@Override
	public List<Lottery> requestLatest() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		List<Lottery> lotteries = new ArrayList<Lottery>(15);
		HttpGet httpget = new HttpGet(URL);
		CloseableHttpResponse response = null;
		try {
			response = httpClient.execute(httpget);
			String strResult = EntityUtils.toString(response.getEntity());
			if (StringUtils.isEmpty(strResult)) {
				return lotteries;
			}
			String[] resultHtmls = StringUtils.substringsBetween(strResult, RESULT_START, RESULT_END);
			if (resultHtmls != null) {
				for (String resultHtml : resultHtmls) {
					lotteries.add(createLottery(resultHtml));
				}
				String countString = StringUtils.substringBetween(strResult, COUNT_START, COUNT_END);
				if (!StringUtils.isEmpty(countString)) {
					String cs = countString.trim();
					LotteryManager.instance().setRealCount(Integer.parseInt(cs) - 1);
				}
			}
		} finally {
			if (response != null) {
				response.close();
			}
			httpClient.close();
		}
		Collections.reverse(lotteries);
		return lotteries;
	}

	private Lottery createLottery(String resultHtml) throws ParseException {
		Lottery lottery = new Lottery();
		lottery.setTerm((int) (Long.parseLong(resultHtml.substring(0, 11)) - Commons.TERM_SART));
		Date date = dateFormat.parse(StringUtils.substringBetween(resultHtml, "<td>", "</td>").trim());
		lottery.setTime((int) ((date.getTime() - Commons.TIME_SART) / BASE_TIME));
		String numStrings = StringUtils.substringBetween(resultHtml, "<span>", "</div>");
		numStrings = pattern.matcher(numStrings).replaceAll("").trim();
		lottery.setNumber(Integer.parseInt(numStrings));
		return lottery;
	}
}
