import React from "react";

const TradeMetrics = ({ metrics }) => {
  return (
    <div className="trade-metrics">
      <div className="metric-item">
        <span>WINS</span>
        <h4>{metrics?.wins || 0}</h4>
        <p className="percentage">{metrics?.winRate || 0}%</p>
      </div>
      <div className="metric-item">
        <span>OPEN</span>
        <h4>{metrics?.open || 0}</h4>
      </div>
      <div className="metric-item">
        <span>AVG W</span>
        <h4>₹{metrics?.avgWin || 0}</h4>
      </div>
      <div className="metric-item losses">
        <span>LOSSES</span>
        <h4>{metrics?.losses || 0}</h4>
        <p className="percentage negative">{metrics?.lossRate || 0}%</p>
      </div>
      <div className="metric-item">
        <span>WASH</span>
        <h4>{metrics?.wash || 0}</h4>
      </div>
      <div className="metric-item">
        <span>AVG L</span>
        <h4 className="negative">₹{metrics?.avgLoss || 0}</h4>
      </div>
    </div>
  );
};

export default TradeMetrics;
