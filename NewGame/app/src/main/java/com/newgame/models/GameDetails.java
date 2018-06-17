package com.newgame.models;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @return
 * The GetGameDetailsResult
 */
/**
 * 
 *            The GetGameDetailsResult
 */
public class GameDetails {

	@Expose
	private List<GameDetailsResult> GetGameDetailsResult = new ArrayList<GameDetailsResult>();

	/**
	 * 
	 * @return The GetGameDetailsResult
	 */
	public List<GameDetailsResult> getGetGameDetailsResult() {
		return GetGameDetailsResult;
	}

	/**
	 * 
	 * @param GetGameDetailsResult
	 *            The GetGameDetailsResult
	 */
	public void setGetGameDetailsResult(
			List<GameDetailsResult> GetGameDetailsResult) {
		this.GetGameDetailsResult = GetGameDetailsResult;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
