# バスくる シリーズ (Archived)

※本リポジトリは、過去にGoogle Playにて公開・運用していた個人開発Androidアプリです。<br/>
当時の検証記録や実装ロジックについては [開発ブログ](https://bobtabo.hatenadiary.org/) にて公開しています。

---

## 📱 アプリ表示イメージ

<p align="left">
  <img src="https://image.winudf.com/v2/image1/b3JnLmJ1c2t1cnUudG9reXVfc2NyZWVuXzBfMTU2NzAwOTg3MV8wNzI/screen-0.webp?fakeurl=1&type=.webp" width="220" alt="バスくる スクリーンショット" />
  <img src="https://image.winudf.com/v2/image1/b3JnLmJ1c2t1cnUudG9reXVfc2NyZWVuXzFfMTU2NzAwOTg3Ml8wNjI/screen-1.webp?fakeurl=1&type=.webp" width="220" alt="バスくる スクリーンショット" />
  <img src="https://image.winudf.com/v2/image1/b3JnLmJ1c2t1cnUudG9reXVfc2NyZWVuXzJfMTU2NzAwOTg3Ml8wNDE/screen-2.webp?fakeurl=1&type=.webp" width="220" alt="バスくる スクリーンショット" />
  <img src="https://image.winudf.com/v2/image1/b3JnLmJ1c2t1cnUudG9reXVfc2NyZWVuXzNfMTU2NzAwOTg3M18wMTI/screen-3.webp?fakeurl=1&type=.webp" width="220" alt="バスくる スクリーンショット" />
  <img src="https://image.winudf.com/v2/image1/b3JnLmJ1c2t1cnUudG9reXVfc2NyZWVuXzRfMTU2NzAwOTg3M18wMzI/screen-4.webp?fakeurl=1&type=.webp" width="220" alt="バスくる スクリーンショット" />
  <img src="https://image.winudf.com/v2/image1/b3JnLmJ1c2t1cnUudG9reXVfc2NyZWVuXzVfMTU2NzAwOTg3NF8wMDE/screen-5.webp?fakeurl=1&type=.webp" width="220" alt="バスくる スクリーンショット" />
  <img src="https://image.winudf.com/v2/image1/b3JnLmJ1c2t1cnUudG9reXVfc2NyZWVuXzZfMTU2NzAwOTg3NF8wNjc/screen-6.webp?fakeurl=1&type=.webp" width="220" alt="バスくる スクリーンショット" />
  <img src="https://image.winudf.com/v2/image1/b3JnLmJ1c2t1cnUudG9reXVfc2NyZWVuXzdfMTU2NzAwOTg3NV8wOTQ/screen-7.webp?fakeurl=1&type=.webp" width="220" alt="バスくる スクリーンショット" />
</p>

## 💡 コアコンセプト：能動的な閲覧から、端末ローカルでの定期自動通知へ
当時のバス接近情報は「ユーザーが自らWebサイトを開いて確認しに行く」スタイルが主流でした。<br/>
本アプリでは**「自分でサイトを見に行くのではなく、接近を教えてくれる」**をコンセプトに開発しました。<br/>
バスが来るから「バスくる」です。

外部通知サーバーに依存せず、`AlarmManager` と `PendingIntent` を活用してアプリ単体でバックグラウンドの定期監視ループを構築し、「あと○分で到着します」といった接近情報をToast等で自動通知する仕組みを初期の段階で実現しました。

### 展開していたプロダクトライン
* [バスくる for 東急](https://github.com/bobtabo/buskuru-android-tokyu)
* [バスくる for 国際興業](https://github.com/bobtabo/buskuru-android-kokusai)
* [バスくる for 京急](https://github.com/bobtabo/buskuru-android-keikyu)
* [バスくる for 小田急](https://github.com/bobtabo/buskuru-android-odakyu)
* [バスくる for 相鉄](https://github.com/bobtabo/buskuru-android-sotetsu)

---

## 🛠 共通基盤（フレームワーク化）へのアプローチ
各バス事業者ごとに異なるWebUIやHTML構造の差異を柔軟に吸収し、効率的に複数アプリを展開するため、以下の構造化を推進しました。

* **コアロジックの共通化:** バックグラウンド定期監視（AlarmManager）・通知・ローカル状態管理のコンポーネント化
* **データ取得層（Scraper）の抽象化:** パース処理をインターフェース化し、新規バス事業者追加時の開発コストを最小化

---

## 📝 開発背景 & アーキテクチャ判断
* **技術スタック:** Java / Android SDK / SQLite / OrmLite / HTML Cleaner / Jackson
* **プロジェクトのクローズ理由:** Google Playのバックグラウンド処理制限・サードパーティアプリに関するストアポリシーの変更、および対象Webサイトの仕様変更に伴うメンテナンスコストの観点から、アーキテクチャの運用継続性を総合的に判断し公開を終了しました。
