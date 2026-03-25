# Behavioural Interview — STAR Framework

## STAR Method
```
S — Situation: Set context (team size, company stage, timeline)
T — Task:      Your specific responsibility
A — Action:    What YOU did (use "I", not "we")
R — Result:    Measurable outcome
```

## Common Questions and Story Templates

### "Tell me about a time you solved a difficult technical problem"
```
S: At [Company], our payments service was dropping 0.5% of transactions
   silently — no error, money debited but order not created.

T: I was the on-call engineer and had to root-cause and fix.

A: • Added distributed tracing to payment + order service.
   • Discovered a race condition in async webhook processing.
   • Implemented idempotency key pattern + DB unique constraint.
   • Added DLQ monitoring + alerts for failed messages.
   • Wrote post-mortem and presented to 30-person team.

R: Reduced payment failure rate from 0.5% to 0.001%.
   Saved ~$50K/month in failed transactions.
   Promoted to senior engineer 2 months later.
```

### "Tell me about a time you had conflict with a teammate"
```
S: A senior engineer insisted on a microservices rewrite.
   I believed it was premature given our 5-person team.

T: Make a technical decision while preserving team harmony.

A: • Requested a meeting with both of us + EM.
   • Prepared data: migration cost (3 months), current load (10% capacity).
   • Proposed: "Let's define specific scaling thresholds first.
     If we hit X, we commit to the rewrite."
   • Created shared doc for both approaches' trade-offs.

R: Team agreed on monolith with modular boundaries.
   6 months later hit threshold → started migration with buy-in.
```

### "Tell me about a failure"
```
S: I deployed a DB migration during peak traffic.
   Caused 20-min outage affecting 10K users.

T: I owned the deployment and failed to plan for it.

A: • Immediately rolled back migration.
   • Communicated clearly on Slack: ETR 15 min, status page updated.
   • Post-mortem: identified I skipped deploy checklist.
   • Created automated check: migrations require off-peak schedule.
   • Reviewed all pending migrations with team.

R: Written process now prevents deployment during peak hours.
   No similar incident in 18 months.
   Learned: checklists > memory, especially under pressure.
```

### "Describe a time you improved a process"
```
S: Code reviews were taking 3-4 days. PRs stacked.

T: As tech lead, unblock team velocity.

A: • Analysed: large PRs averaged 4 days, small PRs < 8 hours.
   • Created PR template with size limit (400 lines soft).
   • Added GitHub check: PR > 600 lines → warning comment.
   • Introduced feature flags so partial code could merge safely.
   • Weekly 30-min PR review schedule.

R: Average review time: 4 days → 18 hours (340% improvement).
   Team shipped 2.2x more features per sprint.
```
