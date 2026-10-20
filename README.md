
import React, { useState } from "react";

const temples = [
  {
    id: 1,
    name: "श्री बाँके बिहारी मंदिर",
    city: "वृंदावन, उत्तर प्रदेश",
    emoji: "🛕",
    description: "श्री बाँके बिहारी जी के दर्शन",
    color: "#7045a3",
  },
  {
    id: 2,
    name: "श्री कृष्ण जन्मभूमि",
    city: "मथुरा, उत्तर प्रदेश",
    emoji: "🙏",
    description: "भगवान श्रीकृष्ण की जन्मस्थली",
    color: "#b87530",
  },
  {
    id: 3,
    name: "द्वारकाधीश मंदिर",
    city: "द्वारका, गुजरात",
    emoji: "🪷",
    description: "श्री द्वारकाधीश के दर्शन",
    color: "#286b9e",
  },
  {
    id: 4,
    name: "प्रेम मंदिर",
    city: "वृंदावन, उत्तर प्रदेश",
    emoji: "🌸",
    description: "राधा-कृष्ण की दिव्य लीलाएँ",
    color: "#a63d68",
  },
  {
    id: 5,
    name: "श्रीनाथजी मंदिर",
    city: "नाथद्वारा, राजस्थान",
    emoji: "🌺",
    description: "श्रीनाथजी के दर्शन",
    color: "#32745a",
  },
  {
    id: 6,
    name: "इस्कॉन मंदिर",
    city: "मायापुर, पश्चिम बंगाल",
    emoji: "🦚",
    description: "हरे कृष्ण महामंत्र और दर्शन",
    color: "#375a9c",
  },
];

const aartis = [
  "मंगला आरती",
  "श्रृंगार दर्शन",
  "ग्वाल दर्शन",
  "राजभोग दर्शन",
  "उत्थापन दर्शन",
  "संध्या आरती",
  "शयन दर्शन",
];

const offerings = [
  "माखन-मिश्री",
  "फल",
  "फूल",
  "तुलसी दल",
  "लड्डू",
  "पंचामृत",
];

export default function App() {
  const [page, setPage] = useState("home");
  const [search, setSearch] = useState("");
  const [selectedTemple, setSelectedTemple] = useState(temples[0]);
  const [offering, setOffering] = useState("");
  const [offered, setOffered] = useState([]);
  const [lampOn, setLampOn] = useState(false);
  const [bellCount, setBellCount] = useState(0);
  const [message, setMessage] = useState("");

  const filteredTemples = temples.filter((temple) =>
    `${temple.name} ${temple.city}`
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  function navigate(nextPage) {
    setPage(nextPage);
    setMessage("");
  }

  function openTemple(temple) {
    setSelectedTemple(temple);
    setPage("darshan");
    setMessage("");
  }

  function offerBhog() {
    if (!offering) {
      setMessage("कृपया पहले भोग का चयन करें।");
      return;
    }

    setOffered((previous) => [...previous, offering]);
    setMessage(`आपने ${offering} का वर्चुअल भोग अर्पित किया।`);
    setOffering("");
  }

  return (
    <div className="app">
      <style>{`
        * { box-sizing: border-box; }
        body { margin: 0; }
        .app {
          min-height: 100vh;
          background: #f8f4eb;
          color: #292039;
          font-family: system-ui, sans-serif;
        }
        button, input, select { font: inherit; }
        button { cursor: pointer; }
        .header {
          background: #24123c;
          color: white;
          padding: 18px 16px;
          text-align: center;
          border-bottom: 3px solid #d9ad60;
        }
        .brand { font-size: 24px; font-weight: 800; }
        .subtitle { color: #f2dba6; margin-top: 7px; }
        .nav {
          display: flex;
          gap: 8px;
          padding: 12px;
          overflow-x: auto;
          background: #fff;
          border-bottom: 1px solid #e8dfd0;
        }
        .nav button {
          white-space: nowrap;
          border: 1px solid #e4d5bd;
          background: #fffaf1;
          padding: 9px 13px;
          border-radius: 22px;
          color: #412458;
        }
        .nav button.active {
          background: #5b287b;
          color: white;
        }
        main { max-width: 1050px; margin: auto; padding: 20px 14px 40px; }
        .hero {
          text-align: center;
          color: white;
          padding: 35px 18px;
          border-radius: 20px;
          background: linear-gradient(135deg,#281348,#7541a0,#bd7c36);
        }
        .hero h1 { font-size: clamp(27px,5vw,42px); margin: 0 0 12px; }
        .hero p { line-height: 1.8; }
        .btn {
          border: none;
          border-radius: 10px;
          padding: 12px 16px;
          background: #6b338c;
          color: white;
          font-weight: 700;
        }
        .btn.gold { background: #e7c17b; color: #301942; }
        .section-title { margin: 27px 0 14px; font-size: 22px; }
        .grid {
          display: grid;
          grid-template-columns: repeat(auto-fit,minmax(220px,1fr));
          gap: 14px;
        }
        .card {
          background: white;
          border: 1px solid #ece2d3;
          border-radius: 15px;
          overflow: hidden;
          box-shadow: 0 3px 12px #3c24420a;
        }
        .temple-art {
          height: 125px;
          display: grid;
          place-items: center;
          font-size: 60px;
          color: white;
        }
        .card-content { padding: 15px; }
        .card h3 { margin: 0 0 7px; }
        .muted { color: #746a7b; font-size: 14px; line-height: 1.6; }
        .search {
          width: 100%;
          padding: 13px;
          border: 1px solid #d9cdbb;
          border-radius: 10px;
          margin: 10px 0 16px;
          background: white;
        }
        .panel {
          background: white;
          border: 1px solid #ece2d3;
          border-radius: 16px;
          padding: 20px;
          margin: 15px 0;
        }
        .center { text-align: center; }
        .big-icon { font-size: 80px; margin: 10px; }
        .items { display: flex; flex-wrap: wrap; gap: 9px; }
        .items button {
          border: 1px solid #dfd0e9;
          background: #fbf5ff;
          color: #4d2869;
          border-radius: 10px;
          padding: 11px;
        }
        .items button.selected { background: #5b287b; color: white; }
        .notice {
          background: #fff5d9;
          border-left: 4px solid #c99a3c;
          padding: 12px;
          border-radius: 5px;
          line-height: 1.6;
          font-size: 14px;
        }
        .footer {
          text-align: center;
          padding: 22px;
          background: #24123c;
          color: #f5e6c5;
        }
        .spaced { display: flex; flex-wrap: wrap; gap: 10px; }
      `}</style>

      <header className="header">
        <div className="brand">🦚 श्री कृष्ण विश्व दर्शन</div>
        <div className="subtitle">हरे कृष्ण हरे कृष्ण, कृष्ण कृष्ण हरे हरे</div>
      </header>

      <nav className="nav">
        {[
          ["home", "🏠 मुख्य पृष्ठ"],
          ["temples", "🛕 सभी मंदिर"],
          ["aarti", "🪔 आरती"],
          ["puja", "🙏 वर्चुअल पूजा"],
          ["bhog", "🌸 भोग"],
          ["shayan", "🌙 शयन दर्शन"],
        ].map(([id, label]) => (
          <button
            key={id}
            className={page === id ? "active" : ""}
            onClick={() => navigate(id)}
          >
            {label}
          </button>
        ))}
      </nav>

      <main>
        {page === "home" && (
          <>
            <section className="hero">
              <div style={{ fontSize: 48 }}>🦚 🙏 🪷</div>
              <h1>घर बैठे श्री कृष्ण के दर्शन</h1>
              <p>
                भारत और विश्व के कृष्ण मंदिरों की जानकारी,
                आरती, भोग तथा वर्चुअल पूजा का अनुभव।
              </p>
              <button className="btn gold" onClick={() => navigate("temples")}>
                अभी दर्शन करें →
              </button>
            </section>

            <h2 className="section-title">प्रमुख कृष्ण मंदिर</h2>
            <TempleGrid temples={temples.slice(0, 4)} onOpen={openTemple} />

            <h2 className="section-title">आपकी भक्ति के लिए</h2>
            <div className="grid">
              {[
                ["🪔", "दैनिक आरती", "aarti"],
                ["🙏", "वर्चुअल पूजा", "puja"],
                ["🌸", "भोग अर्पण", "bhog"],
                ["🌙", "शयन दर्शन", "shayan"],
              ].map(([emoji, title, id]) => (
                <button
                  key={id}
                  className="card card-content"
                  onClick={() => navigate(id)}
                  style={{ textAlign: "left", color: "#39234c" }}
                >
                  <div style={{ fontSize: 32 }}>{emoji}</div>
                  <strong>{title}</strong>
                  <p className="muted">अनुभव के लिए खोलें →</p>
                </button>
              ))}
            </div>
          </>
        )}

        {page === "temples" && (
          <>
            <h1>विश्व के कृष्ण मंदिर</h1>
            <p className="muted">
              मंदिर का नाम या शहर लिखकर खोजें।
            </p>
            <input
              className="search"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="मंदिर खोजें..."
            />
            <TempleGrid temples={filteredTemples} onOpen={openTemple} />
            {filteredTemples.length === 0 && (
              <p>कोई मंदिर नहीं मिला। दूसरे नाम से खोजें।</p>
            )}
          </>
        )}

        {page === "darshan" && (
          <>
            <h1>{selectedTemple.name}</h1>
            <p className="muted">{selectedTemple.city}</p>
            <section
              className="panel center"
              style={{
                background: `linear-gradient(140deg,${selectedTemple.color},#24123c)`,
                color: "white",
                padding: 35,
              }}
            >
              <div style={{ fontSize: 90 }}>{selectedTemple.emoji}</div>
              <h2>{selectedTemple.description}</h2>
              <p>जय श्री राधे कृष्ण</p>
            </section>
            <div className="notice">
              यह प्रारंभिक वर्चुअल दृश्य है, मंदिर की वास्तविक
              लाइव वीडियो स्ट्रीम नहीं।
            </div>
            <div className="panel">
              <h3>दर्शन के विकल्प</h3>
              <div className="spaced">
                <button className="btn" onClick={() => navigate("aarti")}>
                  आरती देखें
                </button>
                <button className="btn" onClick={() => navigate("puja")}>
                  पूजा करें
                </button>
                <button className="btn" onClick={() => navigate("bhog")}>
                  भोग अर्पित करें
                </button>
              </div>
              <p className="muted">
                आधिकारिक लाइव दर्शन का लिंक व्यवस्थापक द्वारा
                सत्यापित होने के बाद यहाँ जोड़ा जा सकता है।
              </p>
            </div>
          </>
        )}

        {page === "aarti" && (
          <>
            <h1>🪔 आरती एवं दर्शन</h1>
            <p className="muted">
              अपनी पसंद की आरती चुनें।
            </p>
            <div className="grid">
              {aartis.map((name, index) => (
                <div className="card card-content" key={name}>
                  <div style={{ fontSize: 32 }}>🪔</div>
                  <h3>{name}</h3>
                  <p className="muted">
                    मंदिर के अनुसार समय अलग हो सकता है।
                  </p>
                  <button
                    className="btn"
                    onClick={() => {
                      setMessage(`${name} चुनी गई।`);
                    }}
                  >
                    आरती चुनें
                  </button>
                </div>
              ))}
            </div>
            {message && <p className="notice">{message}</p>}
            <p className="muted">
              वास्तविक आरती वीडियो और समय-सारणी जोड़ने के लिए
              संबंधित मंदिर का सत्यापित स्रोत आवश्यक है।
            </p>
          </>
        )}

        {page === "puja" && (
          <>
            <h1>🙏 वर्चुअल पूजा</h1>
            <section className="panel center">
              <div className="big-icon">{lampOn ? "🔥" : "🪔"}</div>
              <h2>{lampOn ? "दीपक प्रज्वलित है" : "दीपक जलाएँ"}</h2>
              <button
                className="btn"
                onClick={() => setLampOn(!lampOn)}
              >
                {lampOn ? "दीपक बुझाएँ" : "दीपक जलाएँ"}
              </button>
            </section>
            <section className="panel center">
              <div className="big-icon">🔔</div>
              <h3>मंदिर की घंटी</h3>
              <button
                className="btn"
                onClick={() => setBellCount((n) => n + 1)}
              >
                घंटी बजाएँ
              </button>
              <p className="muted">
                घंटी बजाने की संख्या: {bellCount}
              </p>
            </section>
            <div className="notice">
              यह वर्चुअल पूजा है। इसमें वास्तविक ध्वनि या
              भौतिक अर्पण तभी होगा जब संबंधित सुविधा जोड़ी जाए।
            </div>
          </>
        )}

        {page === "bhog" && (
          <>
            <h1>🌸 श्री कृष्ण भोग अर्पण</h1>
            <section className="panel">
              <h3>अपना भोग चुनें</h3>
              <div className="items">
                {offerings.map((item) => (
                  <button
                    key={item}
                    className={offering === item ? "selected" : ""}
                    onClick={() => setOffering(item)}
                  >
                    {item}
                  </button>
                ))}
              </div>
              <button
                className="btn"
                style={{ marginTop: 20 }}
                onClick={offerBhog}
              >
                भगवान को भोग अर्पित करें
              </button>
              {message && <p className="notice">{message}</p>}
              {offered.length > 0 && (
                <>
                  <h3>आपके वर्चुअल अर्पण</h3>
                  <ul>
                    {offered.map((item, i) => (
                      <li key={i}>{item}</li>
                    ))}
                  </ul>
                </>
              )}
            </section>
          </>
        )}

        {page === "shayan" && (
          <>
            <section className="hero">
              <div style={{ fontSize: 65 }}>🌙 🪷 🪔</div>
              <h1>श्री कृष्ण शयन दर्शन</h1>
              <p>
                रात्रि की शांति में भगवान श्रीकृष्ण का स्मरण करें।
              </p>
            </section>
            <section className="panel center">
              <h2>हरे कृष्ण महामंत्र</h2>
              <p style={{ lineHeight: 2 }}>
                हरे कृष्ण हरे कृष्ण, कृष्ण कृष्ण हरे हरे।
                <br />
                हरे राम हरे राम, राम राम हरे हरे॥
              </p>
              <button
                className="btn"
                onClick={() => setMessage("महामंत्र का स्मरण करें।")}
              >
                मंत्र स्मरण
              </button>
              {message && <p>{message}</p>}
            </section>
          </>
        )}
      </main>

      <footer className="footer">
        <div>🌺 श्री कृष्ण विश्व दर्शन 🌺</div>
        <p>भक्ति • दर्शन • आरती • सेवा</p>
        <small>
          वर्चुअल पूजा वास्तविक मंदिर पूजा का विकल्प नहीं है।
        </small>
      </footer>
    </div>
  );
}

function TempleGrid({ temples, onOpen }) {
  return (
    <div className="grid">
      {temples.map((temple) => (
        <article className="card" key={temple.id}>
          <div
            className="temple-art"
            style={{
              background: `linear-gradient(135deg,${temple.color},#24123c)`,
            }}
          >
            {temple.emoji}
          </div>
          <div className="card-content">
            <h3>{temple.name}</h3>
            <div className="muted">{temple.city}</div>
            <p className="muted">{temple.description}</p>
            <button
              className="btn"
              onClick={() => onOpen(temple)}
            >
              दर्शन करें →
            </button>
          </div>
        </article>
      ))}
    </div>
  );
}

import React, { useState } from "react";

const temples = [
  {
    id: 1,
    name: "श्री बाँके बिहारी मंदिर",
    city: "वृंदावन, उत्तर प्रदेश",
    image:
      "https://images.unsplash.com/photo-1606293926075-69a00dbfde81?auto=format&fit=crop&w=1000&q=80",
    description: "वृंदावन का प्रसिद्ध श्री बाँके बिहारी मंदिर।",
    website: "https://www.bihariji.org/",
    live: "",
  },
  {
    id: 2,
    name: "प्रेम मंदिर",
    city: "वृंदावन, उत्तर प्रदेश",
    image:
      "https://images.unsplash.com/photo-1561361513-2d000a50f0dc?auto=format&fit=crop&w=1000&q=80",
    description: "सुंदर वास्तुकला और राधा-कृष्ण की लीलाओं के लिए प्रसिद्ध।",
    website: "",
    live: "",
  },
  {
    id: 3,
    name: "श्री द्वारकाधीश मंदिर",
    city: "द्वारका, गुजरात",
    image:
      "https://images.unsplash.com/photo-1514222134-b57cbb8ce073?auto=format&fit=crop&w=1000&q=80",
    description: "भगवान श्रीकृष्ण को समर्पित प्राचीन तीर्थस्थल।",
    website: "",
    live: "",
  },
  {
    id: 4,
    name: "श्री कृष्ण जन्मभूमि",
    city: "मथुरा, उत्तर प्रदेश",
    image:
      "https://images.unsplash.com/photo-1605640840605-14ac1855827b?auto=format&fit=crop&w=1000&q=80",
    description: "मथुरा का प्रमुख कृष्ण तीर्थस्थल।",
    website: "",
    live: "",
  },
  {
    id: 5,
    name: "श्रीनाथजी मंदिर",
    city: "नाथद्वारा, राजस्थान",
    image:
      "https://images.unsplash.com/photo-1548013146-72479768bada?auto=format&fit=crop&w=1000&q=80",
    description: "श्रीनाथजी की सेवा और दर्शन की प्रसिद्ध परंपरा।",
    website: "",
    live: "",
  },
  {
    id: 6,
    name: "इस्कॉन मायापुर",
    city: "मायापुर, पश्चिम बंगाल",
    image:
      "https://images.unsplash.com/photo-1604608672516-8b5e4f6a9c20?auto=format&fit=crop&w=1000&q=80",
    description: "हरे कृष्ण भक्ति का प्रमुख आध्यात्मिक केंद्र।",
    website: "https://www.mayapur.com/",
    live: "",
  },
];

export default function App() {
  const [search, setSearch] = useState("");
  const [selected, setSelected] = useState(null);
  const [fullImage, setFullImage] = useState(false);

  const results = temples.filter((t) =>
    `${t.name} ${t.city}`.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className="min-h-screen bg-amber-50 text-purple-950">
      <header className="bg-purple-950 text-white p-6 text-center">
        <div className="text-4xl">🦚</div>
        <h1 className="text-3xl font-bold mt-2">
          श्री कृष्ण विश्व दर्शन
        </h1>
        <p className="text-amber-200 mt-2">
          हरे कृष्ण हरे कृष्ण, कृष्ण कृष्ण हरे हरे
        </p>
      </header>

      <main className="max-w-6xl mx-auto p-4 sm:p-6">
        <section className="rounded-2xl bg-gradient-to-r from-purple-900 to-amber-700 text-white p-7 sm:p-10 mb-8">
          <h2 className="text-3xl font-bold">
            विश्व के कृष्ण मंदिरों के दर्शन
          </h2>
          <p className="mt-3 leading-7">
            मंदिर खोजें, तस्वीरें देखें और उपलब्ध आधिकारिक
            दर्शन लिंक तक पहुँचें।
          </p>
        </section>

        <input
          className="w-full border border-amber-300 rounded-xl p-4 mb-6 bg-white"
          placeholder="मंदिर या शहर खोजें..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
        />

        {!selected ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {results.map((temple) => (
              <article
                key={temple.id}
                className="bg-white rounded-2xl shadow-md overflow-hidden"
              >
                <img
                  src={temple.image}
                  alt={temple.name}
                  loading="lazy"
                  onError={(e) => {
                    e.currentTarget.style.display = "none";
                  }}
                  className="w-full h-56 object-cover"
                />

                <div className="p-5">
                  <h3 className="text-xl font-bold">{temple.name}</h3>
                  <p className="text-gray-600 mt-2">{temple.city}</p>
                  <p className="mt-3 leading-6">{temple.description}</p>

                  <button
                    onClick={() => setSelected(temple)}
                    className="mt-4 w-full bg-purple-800 text-white rounded-xl p-3 font-semibold"
                  >
                    दर्शन करें
                  </button>
                </div>
              </article>
            ))}
          </div>
        ) : (
          <section className="bg-white rounded-2xl shadow p-5 sm:p-8">
            <button
              onClick={() => {
                setSelected(null);
                setFullImage(false);
              }}
              className="mb-5 bg-gray-100 rounded-lg px-4 py-2"
            >
              ← सभी मंदिरों पर वापस जाएँ
            </button>

            <h2 className="text-2xl font-bold">{selected.name}</h2>
            <p className="text-gray-600 mt-2">{selected.city}</p>

            {!fullImage && (
              <img
                src={selected.image}
                alt={selected.name}
                onError={(e) => {
                  e.currentTarget.style.display = "none";
                }}
                className="w-full max-h-[550px] object-cover rounded-xl mt-5"
              />
            )}

            <div className="flex flex-wrap gap-3 mt-5">
              <button
                onClick={() => setFullImage(!fullImage)}
                className="bg-purple-800 text-white rounded-lg px-4 py-3"
              >
                {fullImage ? "तस्वीर वापस करें" : "तस्वीर का विस्तृत दृश्य"}
              </button>

              {selected.website && (
                <a
                  href={selected.website}
                  target="_blank"
                  rel="noreferrer"
                  className="bg-amber-600 text-white rounded-lg px-4 py-3"
                >
                  आधिकारिक वेबसाइट
                </a>
              )}

              {selected.live ? (
                <a
                  href={selected.live}
                  target="_blank"
                  rel="noreferrer"
                  className="bg-green-700 text-white rounded-lg px-4 py-3"
                >
                  लाइव दर्शन देखें
                </a>
              ) : (
                <span className="bg-gray-100 rounded-lg px-4 py-3">
                  लाइव दर्शन लिंक अभी उपलब्ध नहीं है
                </span>
              )}
            </div>

            <p className="mt-6 leading-7">{selected.description}</p>
            <p className="text-sm text-gray-500 mt-4">
              नोट: तस्वीरों की वास्तविक मंदिर से संबद्धता
              और आधिकारिक लाइव लिंक की अलग से पुष्टि आवश्यक है।
            </p>
          </section>
        )}

        {results.length === 0 && (
          <p className="text-center p-8">कोई मंदिर नहीं मिला।</p>
        )}
      </main>

      <footer className="bg-purple-950 text-amber-100 text-center p-6 mt-8">
        🌸 जय श्री राधे कृष्ण 🌸
      </footer>
    </div>
  );
}
