const $ = (id) => document.getElementById(id);
async function loadCases(){
  const box=$('cases'); box.innerHTML='<div class="muted">Loading cases…</div>';
  try{
    const res=await fetch('/api/cases'); const cases=await res.json();
    if(!cases.length){box.innerHTML='<div class="muted">No cases yet.</div>';return;}
    box.innerHTML=cases.map(c=>`<div class="case-row"><div><strong>${escapeHtml(c.id)} · ${escapeHtml(c.title)}</strong><small>${escapeHtml(c.category)}</small></div><span class="tag">${escapeHtml(c.status)}</span></div>`).join('');
  }catch(e){box.innerHTML='<div class="muted">Could not load cases. Is the Java server running?</div>';}
}
function escapeHtml(s){return String(s??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));}
$('searchForm').addEventListener('submit',async e=>{
  e.preventDefault();const q=$('query').value.trim();const status=$('searchStatus'),box=$('results');
  if(!q){status.textContent='Enter a search term first.';return;}
  status.textContent='Searching…';box.innerHTML='';
  try{
    const res=await fetch('/api/search?q='+encodeURIComponent(q));const data=await res.json();
    status.textContent=`${data.count} result(s) for “${data.query}”.`;
    if(!data.results.length){box.innerHTML='<div class="muted">No matching documents. Try “contract”, “tenant”, or “employment”.</div>';return;}
    box.innerHTML=data.results.map(d=>`<article class="result"><span class="tag">${escapeHtml(d.category)}</span><h3>${escapeHtml(d.id)} · ${escapeHtml(d.title)}</h3><p>${escapeHtml(d.text)}</p></article>`).join('');
  }catch(err){status.textContent='Search failed. Check that the Java server is running.';}
});
$('refreshCases').addEventListener('click',loadCases);
$('caseForm').addEventListener('submit',async e=>{
  e.preventDefault();const form=new FormData(e.target);const body=new URLSearchParams(form);
  try{const res=await fetch('/api/add-case',{method:'POST',headers:{'Content-Type':'application/x-www-form-urlencoded'},body});const data=await res.json();if(!res.ok)throw new Error(data.error||'Could not save case');$('caseStatus').textContent='Case saved to data/cases.csv.';e.target.reset();loadCases();}
  catch(err){$('caseStatus').textContent='Could not save case: '+err.message;}
});
loadCases();
