// ==================== API Configuration ====================
const API_BASE = 'http://localhost:8080';

// ==================== State Management ====================
let analysisData = {
    divCount: 0,
    urlSimilarity: 0,
    cssCount: 0,
    headCount: 0,
    urlComparison: null,
    selectors: [],
    headContent: []
};

let chartsInstances = {
    metricsChart: null,
    riskChart: null
};

// ==================== Initialization ====================
document.addEventListener('DOMContentLoaded', function() {
    console.log('WebID Dashboard loaded');
    initializeCharts();
    // Auto-run initial analysis
    setTimeout(() => {
        analyzeHtml();
        analyzeCss();
        extractHead();
    }, 500);
});

// ==================== Chart Initialization ====================
function initializeCharts() {
    const metricsCtx = document.getElementById('metricsChart');
    const riskCtx = document.getElementById('riskChart');

    if (metricsCtx) {
        chartsInstances.metricsChart = new Chart(metricsCtx, {
            type: 'bar',
            data: {
                labels: ['HTML Structure', 'CSS Selectors', 'Head Metadata', 'URL Risk'],
                datasets: [{
                    label: 'Metrics',
                    data: [0, 0, 0, 0],
                    backgroundColor: [
                        'rgba(0, 255, 136, 0.3)',
                        'rgba(0, 212, 255, 0.3)',
                        'rgba(255, 0, 128, 0.3)',
                        'rgba(255, 170, 0, 0.3)'
                    ],
                    borderColor: [
                        'rgb(0, 255, 136)',
                        'rgb(0, 212, 255)',
                        'rgb(255, 0, 128)',
                        'rgb(255, 170, 0)'
                    ],
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                indexAxis: 'y',
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    x: {
                        beginAtZero: true,
                        max: 100,
                        grid: {
                            color: 'rgba(0, 255, 136, 0.1)'
                        },
                        ticks: {
                            color: 'rgba(255, 255, 255, 0.7)'
                        }
                    },
                    y: {
                        grid: {
                            color: 'rgba(0, 255, 136, 0.1)'
                        },
                        ticks: {
                            color: 'rgba(255, 255, 255, 0.7)'
                        }
                    }
                }
            }
        });
    }

    if (riskCtx) {
        chartsInstances.riskChart = new Chart(riskCtx, {
            type: 'doughnut',
            data: {
                labels: ['URL Risk', 'HTML Risk', 'CSS Risk', 'Metadata Risk', 'Safe'],
                datasets: [{
                    data: [0, 0, 0, 0, 100],
                    backgroundColor: [
                        'rgba(255, 170, 0, 0.6)',
                        'rgba(255, 51, 51, 0.6)',
                        'rgba(255, 0, 128, 0.6)',
                        'rgba(255, 102, 0, 0.6)',
                        'rgba(0, 255, 136, 0.6)'
                    ],
                    borderColor: [
                        'rgb(255, 170, 0)',
                        'rgb(255, 51, 51)',
                        'rgb(255, 0, 128)',
                        'rgb(255, 102, 0)',
                        'rgb(0, 255, 136)'
                    ],
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        position: 'bottom',
                        labels: {
                            color: 'rgba(255, 255, 255, 0.8)',
                            padding: 15
                        }
                    }
                }
            }
        });
    }
}

// ==================== API Handlers ====================

/**
 * Analyze HTML: Count closing div tags
 */
async function analyzeHtml() {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE}/run/divcounter`);
        const data = await response.json();
        
        if (data.status === 'success') {
            analysisData.divCount = data.divCount;
            
            // Update UI
            document.getElementById('divCount').textContent = data.divCount;
            document.getElementById('divDetail').textContent = `Found ${data.divCount} closing div tags`;
            document.getElementById('divStatus').textContent = 'success';
            document.getElementById('divStatus').className = 'status-badge success';
            
            console.log('HTML analysis complete:', data);
            updateRiskScores();
            updateCharts();
        }
    } catch (error) {
        console.error('Error analyzing HTML:', error);
        document.getElementById('divStatus').textContent = 'error';
        document.getElementById('divStatus').className = 'status-badge error';
    } finally {
        showLoading(false);
    }
}

/**
 * Analyze CSS: Get non-standard selectors
 */
async function analyzeCss() {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE}/run/readcss`);
        const data = await response.json();
        
        if (data.status === 'success') {
            analysisData.cssCount = data.selectorCount;
            analysisData.selectors = data.selectors;
            
            // Update UI
            document.getElementById('cssCount').textContent = data.selectorCount;
            document.getElementById('cssDetail').textContent = `Found ${data.selectorCount} non-standard selectors`;
            document.getElementById('cssStatus').textContent = 'success';
            document.getElementById('cssStatus').className = 'status-badge success';
            
            // Display selectors
            const selectorsList = document.getElementById('cssSelectors');
            if (data.selectors.length > 0) {
                selectorsList.innerHTML = data.selectors
                    .map(s => `<span class="item">${escapeHtml(s)}</span>`)
                    .join('');
            } else {
                selectorsList.innerHTML = '<p class="placeholder">No non-standard selectors found</p>';
            }
            
            console.log('CSS analysis complete:', data);
            updateRiskScores();
            updateCharts();
        }
    } catch (error) {
        console.error('Error analyzing CSS:', error);
        document.getElementById('cssStatus').textContent = 'error';
        document.getElementById('cssStatus').className = 'status-badge error';
    } finally {
        showLoading(false);
    }
}

/**
 * Extract Head Content
 */
async function extractHead() {
    showLoading(true);
    try {
        const response = await fetch(`${API_BASE}/run/headfile`);
        const data = await response.json();
        
        if (data.status === 'success') {
            analysisData.headCount = data.contentCount;
            analysisData.headContent = data.headContent;
            
            // Update UI
            document.getElementById('headCount').textContent = data.contentCount;
            document.getElementById('headDetail').textContent = `Found ${data.contentCount} metadata tokens`;
            document.getElementById('headStatus').textContent = 'success';
            document.getElementById('headStatus').className = 'status-badge success';
            
            // Display tokens
            const tokensList = document.getElementById('headTokens');
            if (data.headContent.length > 0) {
                tokensList.innerHTML = data.headContent
                    .slice(0, 20) // Show first 20 tokens
                    .map(t => `<span class="item">${escapeHtml(t)}</span>`)
                    .join('');
                
                if (data.headContent.length > 20) {
                    tokensList.innerHTML += `<span class="item">+${data.headContent.length - 20} more...</span>`;
                }
            } else {
                tokensList.innerHTML = '<p class="placeholder">No head content found</p>';
            }
            
            console.log('Head content extraction complete:', data);
            updateRiskScores();
            updateCharts();
        }
    } catch (error) {
        console.error('Error extracting head content:', error);
        document.getElementById('headStatus').textContent = 'error';
        document.getElementById('headStatus').className = 'status-badge error';
    } finally {
        showLoading(false);
    }
}

/**
 * Compare URLs
 */
async function performUrlComparison() {
    const url1 = document.getElementById('urlInput1').value;
    const url2 = document.getElementById('urlInput2').value;

    if (!url1 || !url2) {
        alert('Please enter both URLs');
        return;
    }

    showLoading(true);
    try {
        const params = new URLSearchParams({
            url1: url1,
            url2: url2
        });
        
        const response = await fetch(`${API_BASE}/run/urlcompare?${params}`);
        const data = await response.json();
        
        if (data.status === 'success') {
            analysisData.urlComparison = data;
            analysisData.urlSimilarity = data.similarity;
            
            // Update UI
            document.getElementById('urlSimilarity').textContent = data.similarity.toFixed(1);
            document.getElementById('urlDetail').textContent = `Similarity: ${data.similarity.toFixed(1)}%`;
            document.getElementById('urlStatus').textContent = 'success';
            document.getElementById('urlStatus').className = 'status-badge success';
            
            // Update detailed info
            document.getElementById('detailUrl1').textContent = data.url1;
            document.getElementById('detailUrl2').textContent = data.url2;
            document.getElementById('detailMatches').textContent = data.matches;
            
            console.log('URL comparison complete:', data);
            closeUrlModal();
            updateRiskScores();
            updateCharts();
        }
    } catch (error) {
        console.error('Error comparing URLs:', error);
        alert('Error comparing URLs');
        document.getElementById('urlStatus').textContent = 'error';
        document.getElementById('urlStatus').className = 'status-badge error';
    } finally {
        showLoading(false);
    }
}

// ==================== Risk Scoring System ====================

/**
 * Calculate and update risk scores
 */
function updateRiskScores() {
    const risks = {
        urlRisk: calculateUrlRisk(),
        htmlRisk: calculateHtmlRisk(),
        cssRisk: calculateCssRisk(),
        metaRisk: calculateMetaRisk()
    };

    const totalRisk = (risks.urlRisk + risks.htmlRisk + risks.cssRisk + risks.metaRisk);
    const riskStatus = getRiskStatus(totalRisk);
    
    // Update risk meter
    document.getElementById('riskScore').textContent = Math.round(totalRisk);
    document.getElementById('riskUrl').textContent = Math.round(risks.urlRisk) + '%';
    document.getElementById('riskHtml').textContent = Math.round(risks.htmlRisk) + '%';
    document.getElementById('riskCss').textContent = Math.round(risks.cssRisk) + '%';
    document.getElementById('riskMeta').textContent = Math.round(risks.metaRisk) + '%';
    
    // Update risk label and color
    const riskLabel = document.querySelector('.risk-label');
    riskLabel.textContent = riskStatus;
    
    const riskGauge = document.querySelector('.risk-gauge');
    if (totalRisk > 75) {
        riskGauge.style.boxShadow = '0 0 30px rgba(255, 51, 51, 0.5), inset 0 0 20px rgba(0, 0, 0, 0.5)';
    } else if (totalRisk > 50) {
        riskGauge.style.boxShadow = '0 0 30px rgba(255, 170, 0, 0.5), inset 0 0 20px rgba(0, 0, 0, 0.5)';
    } else {
        riskGauge.style.boxShadow = '0 0 30px rgba(0, 255, 136, 0.3), inset 0 0 20px rgba(0, 0, 0, 0.5)';
    }
}

/**
 * Calculate URL similarity risk (0-40 points)
 */
function calculateUrlRisk() {
    if (!analysisData.urlComparison) return 0;
    
    const similarity = analysisData.urlSimilarity;
    // High similarity = high risk
    if (similarity > 80) return 40;
    if (similarity > 70) return 30;
    if (similarity > 60) return 20;
    if (similarity > 50) return 10;
    return 0;
}

/**
 * Calculate HTML structure risk (0-20 points)
 */
function calculateHtmlRisk() {
    const divCount = analysisData.divCount;
    
    // Excessive div tags indicate deep nesting or suspicious structure
    if (divCount > 100) return 20;
    if (divCount > 75) return 15;
    if (divCount > 50) return 10;
    if (divCount > 25) return 5;
    return 0;
}

/**
 * Calculate CSS risk (0-25 points)
 */
function calculateCssRisk() {
    const cssCount = analysisData.cssCount;
    
    // Many custom selectors might indicate obfuscation
    if (cssCount > 50) return 25;
    if (cssCount > 30) return 18;
    if (cssCount > 15) return 12;
    if (cssCount > 5) return 6;
    return 0;
}

/**
 * Calculate metadata risk (0-15 points)
 */
function calculateMetaRisk() {
    const headCount = analysisData.headCount;
    
    // Excessive metadata might indicate malicious scripts
    if (headCount > 100) return 15;
    if (headCount > 75) return 12;
    if (headCount > 50) return 8;
    if (headCount > 25) return 4;
    return 0;
}

/**
 * Get risk status label
 */
function getRiskStatus(riskScore) {
    if (riskScore > 80) return '🚨 PHISHING SUSPECTED';
    if (riskScore > 60) return '⚠️ HIGH RISK';
    if (riskScore > 40) return '⚠️ MEDIUM RISK';
    if (riskScore > 20) return '⚠️ LOW RISK';
    return '✅ SAFE';
}

// ==================== Chart Updates ====================

/**
 * Update all charts with current data
 */
function updateCharts() {
    updateMetricsChart();
    updateRiskChart();
}

/**
 * Update metrics bar chart
 */
function updateMetricsChart() {
    if (!chartsInstances.metricsChart) return;
    
    chartsInstances.metricsChart.data.datasets[0].data = [
        analysisData.divCount > 100 ? 100 : analysisData.divCount,
        analysisData.cssCount > 100 ? 100 : analysisData.cssCount,
        analysisData.headCount > 100 ? 100 : analysisData.headCount,
        analysisData.urlSimilarity
    ];
    chartsInstances.metricsChart.update();
}

/**
 * Update risk doughnut chart
 */
function updateRiskChart() {
    if (!chartsInstances.riskChart) return;
    
    const urlRisk = calculateUrlRisk();
    const htmlRisk = calculateHtmlRisk();
    const cssRisk = calculateCssRisk();
    const metaRisk = calculateMetaRisk();
    const safeRisk = Math.max(0, 100 - (urlRisk + htmlRisk + cssRisk + metaRisk));
    
    chartsInstances.riskChart.data.datasets[0].data = [
        urlRisk,
        htmlRisk,
        cssRisk,
        metaRisk,
        safeRisk
    ];
    chartsInstances.riskChart.update();
}

// ==================== Modal Handlers ====================

/**
 * Show URL comparison modal
 */
function showUrlModal() {
    document.getElementById('urlModal').classList.remove('hidden');
}

/**
 * Close URL comparison modal
 */
function closeUrlModal() {
    document.getElementById('urlModal').classList.add('hidden');
}

// ==================== Loading Indicator ====================

/**
 * Show/hide loading indicator
 */
function showLoading(show) {
    const loading = document.getElementById('loadingIndicator');
    if (show) {
        loading.classList.remove('hidden');
    } else {
        loading.classList.add('hidden');
    }
}

// ==================== Utility Functions ====================

/**
 * Escape HTML special characters
 */
function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// ==================== Event Listeners ====================

// Close modal when clicking outside
window.addEventListener('click', function(event) {
    const modal = document.getElementById('urlModal');
    if (event.target === modal) {
        closeUrlModal();
    }
});

// Allow Enter key to trigger URL comparison
document.addEventListener('keypress', function(event) {
    if (event.key === 'Enter') {
        const modal = document.getElementById('urlModal');
        if (!modal.classList.contains('hidden')) {
            performUrlComparison();
        }
    }
});
