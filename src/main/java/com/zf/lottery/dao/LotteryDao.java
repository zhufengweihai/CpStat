package com.zf.lottery.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zf.lottery.common.Commons;
import com.zf.lottery.data.Lottery;
import com.zf.lottery.data.StarType;

public class LotteryDao {
	private static Logger logger = LoggerFactory.getLogger(LotteryDao.class);
	private static final String SQLITE_JDBC = "org.sqlite.JDBC";
	private static final String SQL_DB = "jdbc:sqlite://c:/db/cp.db";

	public void init() throws ClassNotFoundException {
		Class.forName(SQLITE_JDBC);
	}

	public void clear() {
		try {
			DriverManager.deregisterDriver(DriverManager.getDrivers().nextElement());
		} catch (Exception e) {
			logger.error("LotteryDao clear failed", e);
		}
	}

	public List<Lottery> readData() throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		Statement stat = null;
		try {
			stat = conn.createStatement();
			ResultSet rs = stat.executeQuery("select * from lottery");
			List<Lottery> lotteries = new ArrayList<>();
			while (rs.next()) {
				Lottery lottery = new Lottery();
				lottery.setTerm(rs.getInt("term"));
				lottery.setTime(rs.getInt("time"));
				lottery.setNumber(rs.getInt("number"));
				lotteries.add(lottery);
			}
			return lotteries;
		} finally {
			if (stat != null) {
				stat.close();
			}
			conn.close();
		}
	}

	public void saveLatestData(List<Lottery> latestData) throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement("insert into lottery values(?,?,?)");
			int size = latestData.size();
			for (int i = 0; i < size; i++) {
				Lottery lottery = latestData.get(i);
				stat.setInt(1, lottery.getTerm());
				stat.setInt(2, lottery.getTime());
				stat.setInt(3, lottery.getNumber());
				stat.executeUpdate();
			}
		} finally {
			if (stat != null) {
				stat.close();
			}
			conn.close();
		}
	}

	public int getMaxTerm() throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		Statement statement = null;
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("select max(term) from lottery");
			rs.next();
			return rs.getInt(1);
		} finally {
			if (statement != null) {
				statement.close();
			}
			conn.close();
		}
	}

	public int getCount() throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		Statement statement = null;
		try {
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("select count(rowid) from lottery");
			rs.next();
			return rs.getInt(1);
		} finally {
			if (statement != null) {
				statement.close();
			}
			conn.close();
		}
	}

	public int[] getLatestFirstThree() throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		PreparedStatement stat = null;
		int max = LotteryManager.instance().getCount();
		try {
			String sql = "select rowid from lottery where rowid=(select max(rowid) from lottery where number/"
					+ Commons.TWO + "=?)";
			stat = conn.prepareStatement(sql);
			int count = Commons.THREE;
			int[] result = new int[count];
			for (int i = 0; i < count; i++) {
				stat.setInt(1, i);
				ResultSet rs = stat.executeQuery();
				rs.next();
				result[i] = max - rs.getInt(1);
				rs.close();
			}
			return result;
		} finally {
			if (stat != null) {
				stat.close();
			}
			conn.close();
		}
	}

	public int[] getLatestLastThree() throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		PreparedStatement stat = null;
		int max = LotteryManager.instance().getCount();
		try {
			String sql = "select rowid from lottery where rowid=(select max(rowid) from lottery where number%"
					+ Commons.THREE + "=?)";
			stat = conn.prepareStatement(sql);
			int count = Commons.THREE;
			int[] result = new int[count];
			for (int i = 0; i < count; i++) {
				stat.setInt(1, i);
				ResultSet rs = stat.executeQuery();
				rs.next();
				result[i] = max - rs.getInt(1);
				rs.close();
			}
			return result;
		} finally {
			if (stat != null) {
				stat.close();
			}
			conn.close();
		}
	}

	public int[] getLatestFirstTwo() throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		PreparedStatement stat = null;
		int max = LotteryManager.instance().getCount();
		try {
			String sql = "select rowid from lottery where rowid=(select max(rowid) from lottery where number/"
					+ Commons.THREE + "=?)";
			stat = conn.prepareStatement(sql);
			int count = Commons.TWO;
			int[] result = new int[count];
			for (int i = 0; i < count; i++) {
				stat.setInt(1, i);
				ResultSet rs = stat.executeQuery();
				rs.next();
				result[i] = max - rs.getInt(1);
				rs.close();
			}
			return result;
		} finally {
			if (stat != null) {
				stat.close();
			}
			conn.close();
		}
	}

	public int[] getLatestLastTwo() throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		PreparedStatement stat = null;
		int max = LotteryManager.instance().getCount();
		try {
			String sql = "select rowid from lottery where rowid=(select max(rowid) from lottery where number%"
					+ Commons.TWO + "=?)";
			stat = conn.prepareStatement(sql);
			int count = Commons.TWO;
			int[] result = new int[count];
			for (int i = 0; i < count; i++) {
				stat.setInt(1, i);
				ResultSet rs = stat.executeQuery();
				rs.next();
				result[i] = max - rs.getInt(1);
				rs.close();
			}
			return result;
		} finally {
			if (stat != null) {
				stat.close();
			}
			conn.close();
		}
	}

	public int[] getLatestCombTwo() throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		PreparedStatement stat = null;
		int max = LotteryManager.instance().getCount();
		try {
			String sql = "select rowid from lottery where rowid=(select max(rowid) from lottery where number%100 in (?,?))";
			stat = conn.prepareStatement(sql);
			int[] result = new int[55];
			int n = 0;
			for (int i = 0; i < 10; i++) {
				for (int j = i; j < 10; j++) {
					stat.setInt(1, i * 10 + j);
					stat.setInt(2, i + j * 10);
					ResultSet rs = stat.executeQuery();
					rs.next();
					result[n++] = max - rs.getInt(1);
					rs.close();
				}
			}
			return result;
		} finally {
			if (stat != null) {
				stat.close();
			}
			conn.close();
		}
	}

	public int[] getMaxFirstThree() throws SQLException {
		int[] numbers = new int[Commons.THREE];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = i;
		}
		return getMaxAbsence(numbers, StarType.FirstThree.ordinal());
	}

	public int[] getMaxLastThree() throws SQLException {
		int[] numbers = new int[Commons.THREE];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = i;
		}
		return getMaxAbsence(numbers, StarType.LastThree.ordinal());
	}

	public int[] getMaxFirstTwo() throws SQLException {
		int[] numbers = new int[Commons.TWO];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = i;
		}
		return getMaxAbsence(numbers, StarType.FirstTwo.ordinal());
	}

	public int[] getMaxLastTwo() throws SQLException {
		int[] numbers = new int[Commons.TWO];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = i;
		}
		return getMaxAbsence(numbers, StarType.LastTwo.ordinal());
	}

	public int[] getMaxCombThree() throws SQLException {
		int[] numbers = new int[Commons.GROUP_SIX];
		int n = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = i; j < 10; j++) {
				for (int k = j; k < 10; k++) {
					numbers[n++] = i * 100 + j * 10 + k;
				}
			}
		}
		return getMaxAbsence(numbers, StarType.GroupThree.ordinal());
	}

	public int[] getMaxCombTwo() throws SQLException {
		int[] numbers = new int[Commons.COMB_TWO];
		int n = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = i; j < 10; j++) {
				numbers[n++] = i * 10 + j;
			}
		}
		return getMaxAbsence(numbers, StarType.CombTwo.ordinal());
	}

	public int[] getMaxAbsence(int[] numbers, int type) throws SQLException {
		Connection conn = DriverManager.getConnection(SQL_DB);
		PreparedStatement stat = null;
		try {
			String sql = "select max(max) from absence where number=? and type=?";
			stat = conn.prepareStatement(sql);
			int[] result = new int[numbers.length];
			for (int i = 0; i < numbers.length; i++) {
				stat.setInt(1, numbers[i]);
				stat.setInt(2, type);
				ResultSet rs = stat.executeQuery();
				rs.next();
				result[i] = rs.getInt(1);
				rs.close();
			}
			return result;
		} finally {
			if (stat != null) {
				stat.close();
			}
			conn.close();
		}
	}
}
