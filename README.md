# bannerComponent
banner 轮播图，实现自动轮播，indicator 指示

#使用示例:
继承BannerView,实现 getItemView 方法

code:

	public class DemoBannerView extends BannerView<DemoBannerView.Item> {

    private Context mCxt;

    public DemoBannerView(Context context) {
        super(context);
        mCxt = context;
    }

    public DemoBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCxt = context;
    }

    @Override
    public View getItemView(LayoutInflater inflater, Item data, int dataPosition) {
        View view = inflater.inflate(R.layout.item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.pos);
        textView.setText(dataPosition + "-" + data.name);
        imageView.setImageResource(data.resId);
        final int pos = dataPosition;
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mCxt, "click banner item :" + pos, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public static class Item {
        public int resId;
        public String name;
    }
	}


