package com.bld.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharePreferencesOperator {
	static SharePreferencesOperator _builder;

	public static SharePreferencesOperator GetInstence() {
		if (_builder == null)
			_builder = new SharePreferencesOperator();
		return _builder;
	}

	public String getCartData(Context ctx) {
		String rs = "";
		SharedPreferences sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		rs = sp.getString("cart", "");
		return rs;
	}

	public boolean addCartData(Context ctx, String productList) {

		String CartList = getCartData(ctx);
		String[] pro_list = productList.split(",");
		try {
			for (int i = 0; i < pro_list.length; i++) {
				if (!CartList.contains(pro_list[i])) {
					if (CartList.length() > 0)
						CartList += ",";
					CartList += pro_list[i];

				}
			}
			SharedPreferences sp = ctx.getSharedPreferences("SP",
					ctx.MODE_PRIVATE);
			Editor editor = sp.edit();
			editor.putString("cart", CartList);
			editor.commit();
			return true;
		} catch (Exception ex) {
			return false;
		}
	}

	public void updateCartData(Context ctx, String productList) {
		SharedPreferences sp = ctx.getSharedPreferences("SP", ctx.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString("cart", productList);
		editor.commit();
	}
}
